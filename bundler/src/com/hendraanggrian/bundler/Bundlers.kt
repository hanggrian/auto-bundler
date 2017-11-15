@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.bundler

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import com.hendraanggrian.bundler.Bundler.TAG
import com.hendraanggrian.bundler.Bundler.mBindings
import com.hendraanggrian.bundler.Bundler.mDebug
import com.hendraanggrian.bundler.internal.BundleBinding
import com.hendraanggrian.bundler.internal.BundleBinding.Companion.EMPTY
import org.json.JSONObject
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*

/** Bind extra fields in support Fragment with default behavior. */
inline fun android.support.v4.app.Fragment.bindExtras() = arguments!!.bindExtrasTo(this)

/** Bind extra fields in Fragment with default behavior. */
inline fun Fragment.bindExtras() = arguments.bindExtrasTo(this)

/** Bind extra fields in Activity with default behavior. */
inline fun Activity.bindExtras() = intent.bindExtrasTo(this)

/** Bind extra fields in target from source Bundle attached to Intent. */
inline fun Intent.bindExtrasTo(target: Any) = extras.bindExtrasTo(target)

/** Bind extra fields in target from source Bundle. */
fun Bundle.bindExtrasTo(target: Any) {
    val binding = target.javaClass.bindingOf(Extra.SUFFIX, Pair(target.javaClass, target), Pair(Bundle::class.java, this))
    if (mDebug) binding.mSource.print()
}

inline fun extrasOf(targetClass: Class<*>, arg: Any?): Bundle = extrasOf(targetClass, mutableListOf(arg))

inline fun extrasOf(targetClass: Class<*>, vararg args: Any?): Bundle = extrasOf(targetClass, args.toMutableList())

fun extrasOf(targetClass: Class<*>, args: MutableList<*>): Bundle {
    val binding = targetClass.bindingOf(Extra.SUFFIX, Pair(MutableList::class.java, args))
    if (mDebug) binding.mSource.print()
    return binding.mSource
}

/** Bind state fields in target from source Bundle. */
fun Bundle.restoreStatesTo(target: Any) {
    val binding = target.javaClass.bindingOf(State.SUFFIX, Pair(target.javaClass, target), Pair(Bundle::class.java, this))
    if (mDebug) binding.mSource.print()
}

fun Bundle.saveStatesTo(target: Any) {
    val binding = target.javaClass.bindingOf(State.SUFFIX, Pair(Bundle::class.java, this), Pair(target.javaClass, target))
    putAll(binding.mSource)
    if (mDebug) binding.mSource.print()
}

private fun Class<out Any>.bindingOf(targetSuffix: String, vararg constructorParams: Pair<Class<*>, Any>): BundleBinding {
    if (mDebug) d(TAG, "Looking up constructor for $name")
    val constructor = bindingConstructorOf(targetSuffix, constructorParams.map { it.first }.toTypedArray())
    if (constructor == null) {
        if (mDebug) d(TAG, "Ignored because no constructor was found in $simpleName")
        return EMPTY
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

private fun Class<out Any>.bindingConstructorOf(targetSuffix: String, constructorParamTypes: Array<Class<*>>): Constructor<BundleBinding>? {
    if (mBindings == null) mBindings = WeakHashMap<String, Constructor<BundleBinding>>()
    val bindingKey = canonicalName + targetSuffix + constructorParamTypes[0].simpleName
    var binding = mBindings!![bindingKey]
    if (binding != null) {
        if (mDebug) d(TAG, "HIT: Cache found in binding weak map.")
        return binding
    }
    val targetClassName = name
    if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
        if (mDebug) d(TAG, "MISS: Reached framework class. Abandoning search.")
        return null
    }
    @Suppress("UNCHECKED_CAST")
    try {
        binding = classLoader
                .loadClass(targetClassName + targetSuffix)
                .getConstructor(*constructorParamTypes) as Constructor<BundleBinding>
        if (mDebug) d(TAG, "HIT: Loaded binding class, caching in weak map.")
    } catch (e: ClassNotFoundException) {
        if (mDebug) d(TAG, "Not found. Trying superclass ${superclass.name}")
        val targetSuperclass = superclass
        //region abstract classes binding fix
        if (constructorParamTypes[0] == this && constructorParamTypes[0].superclass == targetSuperclass) {
            constructorParamTypes[0] = targetSuperclass
        }
        //endregion
        binding = targetSuperclass.bindingConstructorOf(targetSuffix, constructorParamTypes)
    } catch (e: NoSuchMethodException) {
        throw RuntimeException("Unable to find binding constructor for $targetClassName", e)
    }
    mBindings!![bindingKey] = binding
    return binding
}

private fun Bundle.print() {
    try {
        val jsonObject = JSONObject()
        for (key in keySet()) jsonObject.put(key, get(key))
        val json = jsonObject.toString(4)
        for (line in json.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) d(TAG, line)
    } catch (e: Exception) {
        if (mDebug) e.printStackTrace()
    }
}