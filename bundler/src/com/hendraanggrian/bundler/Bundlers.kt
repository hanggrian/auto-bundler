@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.bundler

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.annotation.RequiresApi
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
fun android.support.v4.app.Fragment.bindExtras() = arguments!! bindExtrasTo this

/** Bind extra fields in Fragment with default behavior, not available pre-11. */
@RequiresApi(11)
fun Fragment.bindExtras() = arguments bindExtrasTo this

/** Bind extra fields in Activity with default behavior. */
fun Activity.bindExtras() = intent bindExtrasTo this

/** Bind extra fields in target from source Bundle attached to Intent. */
infix fun Intent.bindExtrasTo(target: Any) = extras bindExtrasTo target

/** Bind extra fields in target from source Bundle. */
infix fun Bundle.bindExtrasTo(target: Any) {
    val binding = javaClass.bindingOf(Extra.SUFFIX, arrayOf(javaClass, Bundle::class.java), arrayOf(target, this))
    if (mDebug) binding.mSource.print()
}

/** Bind state fields in target from source Bundle. */
infix fun Bundle.bindStatesTo(target: Any) {
    val binding = javaClass.bindingOf(State.SUFFIX, arrayOf(javaClass, Bundle::class.java), arrayOf(target, this))
    if (mDebug) binding.mSource.print()
}

fun extrasOf(targetClass: Class<*>, arg: Any): Bundle = extrasOf(targetClass, listOf(arg))

fun extrasOf(targetClass: Class<*>, vararg args: Any): Bundle = extrasOf(targetClass, args.toList())

fun extrasOf(targetClass: Class<*>, args: List<*>): Bundle {
    val binding = targetClass.bindingOf(Extra.SUFFIX, arrayOf(List::class.java), arrayOf(args))
    if (mDebug) binding.mSource.print()
    return binding.mSource
}

infix fun Bundle.saveStatesTo(target: Any): Bundle {
    val binding = javaClass.bindingOf(State.SUFFIX, arrayOf(Bundle::class.java, javaClass), arrayOf(this, target))
    putAll(binding.mSource)
    if (mDebug) binding.mSource.print()
    return this
}

private fun Class<out Any>.bindingOf(
        targetClassSuffix: String,
        constructorParameterTypes: Array<Class<*>>,
        constructorParameters: Array<Any>
): BundleBinding {
    if (mDebug) d(TAG, "Looking up constructor for " + name)
    val constructor = bindingConstructorOf(targetClassSuffix, constructorParameterTypes)
    if (constructor == null) {
        if (mDebug) d(TAG, "Ignored because no constructor was found in " + simpleName)
        return EMPTY
    }
    try {
        return constructor.newInstance(*constructorParameters)
    } catch (e: IllegalAccessException) {
        throw RuntimeException("Unable to invoke " + constructor, e)
    } catch (e: InstantiationException) {
        throw RuntimeException("Unable to invoke " + constructor, e)
    } catch (e: InvocationTargetException) {
        val cause = e.cause
        if (cause is RuntimeException) throw cause
        if (cause is Error) throw cause
        throw RuntimeException("Unable to create constructor instance.", cause)
    }

}

private fun Class<out Any>.bindingConstructorOf(
        targetClassSuffix: String,
        constructorParameterTypes: Array<Class<*>>
): Constructor<BundleBinding>? {
    if (mBindings == null) mBindings = WeakHashMap<String, Constructor<BundleBinding>>()
    val bindingKey = canonicalName + targetClassSuffix + constructorParameterTypes[0].simpleName
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
                .loadClass(targetClassName + targetClassSuffix)
                .getConstructor(*constructorParameterTypes) as Constructor<BundleBinding>
        if (mDebug) d(TAG, "HIT: Loaded binding class, caching in weak map.")
    } catch (e: ClassNotFoundException) {
        if (mDebug) d(TAG, "Not found. Trying superclass " + superclass.name)
        val targetSuperclass = superclass
        //region abstract classes binding fix
        if (constructorParameterTypes[0] == this && constructorParameterTypes[0].superclass == targetSuperclass) {
            constructorParameterTypes[0] = targetSuperclass
        }
        //endregion
        binding = targetSuperclass.bindingConstructorOf(targetClassSuffix, constructorParameterTypes)
    } catch (e: NoSuchMethodException) {
        throw RuntimeException("Unable to find binding constructor for " + targetClassName, e)
    }
    mBindings!!.put(bindingKey, binding)
    return binding
}

private fun Bundle.print() {
    try {
        val jsonObject = JSONObject()
        for (key in keySet()) {
            jsonObject.put(key, get(key))
        }
        val json = jsonObject.toString(4)
        for (line in json.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            d(TAG, line)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}