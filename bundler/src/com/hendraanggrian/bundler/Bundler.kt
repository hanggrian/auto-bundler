package com.hendraanggrian.bundler

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import com.hendraanggrian.bundler.internal.BundleBinding
import org.json.JSONObject
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.WeakHashMap

@Suppress("NOTHING_TO_INLINE")
object Bundler {

    private const val TAG = "Bundler"
    private var BINDINGS: MutableMap<String, Constructor<BundleBinding>?>? = null
    private var DEBUG: Boolean = false

    /** When set to true, will print Bundler operation in [DEBUG] level. */
    fun setDebug(debug: Boolean) {
        DEBUG = debug
    }

    /** Bind extra fields in support Fragment with default behavior. */
    inline fun bindExtras(target: android.support.v4.app.Fragment) =
        bindExtras(target, target.arguments!!)

    /** Bind extra fields in Fragment with default behavior. */
    inline fun bindExtras(target: Fragment) = bindExtras(target, target.arguments)

    /** Bind extra fields in Activity with default behavior. */
    inline fun bindExtras(target: Activity) = bindExtras(target, target.intent)

    /** Bind extra fields in target from source Bundle attached to Intent. */
    inline fun bindExtras(target: Any, source: Intent) = bindExtras(target, source.extras)

    /** Bind extra fields in target from source Bundle. */
    fun bindExtras(target: Any, source: Bundle) {
        val binding = target.javaClass.bindingOf(Extra.SUFFIX, Pair(target.javaClass, target),
            Pair(Bundle::class.java, source))
        if (DEBUG) binding.bundle.print()
    }

    inline fun extrasOf(targetClass: Class<*>, arg: Any?): Bundle =
        extrasOf(targetClass, mutableListOf(arg))

    inline fun extrasOf(targetClass: Class<*>, vararg args: Any?): Bundle =
        extrasOf(targetClass, args.toMutableList())

    fun extrasOf(targetClass: Class<*>, args: MutableList<*>): Bundle {
        val binding = targetClass.bindingOf(Extra.SUFFIX, Pair(MutableList::class.java, args))
        if (DEBUG) binding.bundle.print()
        return binding.bundle
    }

    /** Bind state fields in target from source Bundle. */
    fun restoreStates(target: Any, source: Bundle) {
        val binding = target.javaClass.bindingOf(State.SUFFIX, Pair(target.javaClass, target),
            Pair(Bundle::class.java, source))
        if (DEBUG) binding.bundle.print()
    }

    fun saveStates(target: Any, source: Bundle) {
        val binding = target.javaClass.bindingOf(State.SUFFIX, Pair(Bundle::class.java, source),
            Pair(target.javaClass, target))
        source.putAll(binding.bundle)
        if (DEBUG) binding.bundle.print()
    }

    private fun Class<out Any>.bindingOf(
        targetSuffix: String,
        vararg constructorParams: Pair<Class<*>, Any>
    ): BundleBinding {
        if (DEBUG) Log.d(TAG, "Looking up constructor for $name")
        val constructor = bindingConstructorOf(targetSuffix,
            constructorParams.map { it.first }.toTypedArray())
        if (constructor == null) {
            if (DEBUG) Log.d(TAG, "Ignored because no constructor was found in $simpleName")
            return BundleBinding.EMPTY
        }
        try {
            return constructor.newInstance(*constructorParams.map { it.second }.toTypedArray())
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to invoke $constructor", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to invoke $constructor", e)
        } catch (e: InvocationTargetException) {
            val cause = e.cause
            if (cause is RuntimeException) throw cause
            if (cause is Error) throw cause
            throw RuntimeException("Unable to create constructor instance.", cause)
        }
    }

    private fun Class<out Any>.bindingConstructorOf(
        targetSuffix: String,
        constructorParamTypes: Array<Class<*>>
    ): Constructor<BundleBinding>? {
        if (BINDINGS == null) BINDINGS = WeakHashMap<String, Constructor<BundleBinding>>()
        val bindingKey = canonicalName + targetSuffix + constructorParamTypes[0].simpleName
        var binding = BINDINGS!![bindingKey]
        if (binding != null) {
            if (DEBUG) Log.d(TAG, "HIT: Cache found in binding weak map.")
            return binding
        }
        val targetClassName = name
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (DEBUG) Log.d(TAG, "MISS: Reached framework class. Abandoning search.")
            return null
        }
        @Suppress("UNCHECKED_CAST")
        try {
            binding = classLoader
                .loadClass(targetClassName + targetSuffix)
                .getConstructor(*constructorParamTypes) as Constructor<BundleBinding>
            if (DEBUG) Log.d(TAG, "HIT: Loaded binding class, caching in weak map.")
        } catch (e: ClassNotFoundException) {
            if (DEBUG) Log.d(TAG, "Not found. Trying superclass ${superclass.name}")
            val targetSuperclass = superclass
            // region abstract classes binding fix
            if (constructorParamTypes[0] == this &&
                constructorParamTypes[0].superclass == targetSuperclass) {
                constructorParamTypes[0] = targetSuperclass
            }
            // endregion
            binding = targetSuperclass.bindingConstructorOf(targetSuffix, constructorParamTypes)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Unable to find binding constructor for $targetClassName", e)
        }
        BINDINGS!![bindingKey] = binding
        return binding
    }

    private fun Bundle.print() {
        try {
            val jsonObject = JSONObject()
            for (key in keySet()) jsonObject.put(key, get(key))
            val json = jsonObject.toString(4)
            for (line in json.split("\\r?\\n".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()) Log.d(TAG, line)
        } catch (e: Exception) {
            if (DEBUG) e.printStackTrace()
        }
    }
}