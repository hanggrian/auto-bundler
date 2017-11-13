package com.hendraanggrian.bundler

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log.d
import com.hendraanggrian.bundler.Bundler.TAG
import com.hendraanggrian.bundler.Bundler.mBindings
import com.hendraanggrian.bundler.Bundler.mDebug
import org.json.JSONObject
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*

object Bundler {

    internal const val TAG = "Bundler"

    internal var mBindings: MutableMap<String, Constructor<BundleBinding>?>? = null
    internal var mDebug: Boolean = false

    fun setDebug(debug: Boolean) {
        mDebug = debug
    }
}

/** Bind extra fields in support Fragment with default behavior. */
fun android.support.v4.app.Fragment.initExtras() = initExtras(arguments!!)

/** Bind extra fields in Fragment with default behavior, not available pre-11. */
@RequiresApi(11)
@TargetApi(11)
fun Fragment.initExtras() = initExtras(arguments)

/** Bind extra fields in Activity with default behavior. */
fun Activity.initExtras() = initExtras(intent)

/** Bind extra fields in target from source Bundle attached to Intent. */
fun Any.initExtras(source: Intent) = initExtras(source.extras)

/** Bind extra fields in target from source Bundle. */
fun Any.initExtras(source: Bundle) {
    val binding = javaClass.createBinding("_ExtraBinding", arrayOf(javaClass, Bundle::class.java), arrayOf(this, source))
    if (mDebug) binding.source.print()
}

/** Bind state fields in target from source Bundle. */
fun Any.bindStates(source: Bundle) {
    val binding = javaClass.createBinding("_StateBinding", arrayOf(javaClass, Bundle::class.java), arrayOf(this, source))
    if (mDebug) binding.source.print()
}

fun wrapExtras(targetClass: Class<*>, arg: Any): Bundle = wrapExtras(targetClass, listOf(arg))

fun wrapExtras(targetClass: Class<*>, vararg args: Any): Bundle = wrapExtras(targetClass, args.toList())

fun wrapExtras(targetClass: Class<*>, args: List<*>): Bundle {
    val binding = targetClass.createBinding("_ExtraBinding", arrayOf(List::class.java), arrayOf(args))
    if (mDebug) binding.source.print()
    return binding.source
}

fun Any.saveStates(source: Bundle) {
    val binding = javaClass.createBinding("_StateBinding", arrayOf(Bundle::class.java, javaClass), arrayOf(source, this))
    source.putAll(binding.source)
    if (mDebug) binding.source.print()
}

private fun Class<out Any>.createBinding(
        targetClassSuffix: String,
        constructorParameterTypes: Array<Class<*>>,
        constructorParameters: Array<Any>
): BundleBinding {
    if (mDebug) d(TAG, "Looking up constructor for " + name)
    val constructor = findBinding(targetClassSuffix, constructorParameterTypes)
    if (constructor == null) {
        if (mDebug) d(TAG, "Ignored because no constructor was found in " + simpleName)
        return BundleBinding.EMPTY
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

@Suppress("UNCHECKED_CAST")
private fun Class<out Any>.findBinding(
        targetClassSuffix: String,
        constructorParameterTypes: Array<Class<*>>
): Constructor<out BundleBinding>? {
    if (mBindings == null) mBindings = WeakHashMap<String, Constructor<BundleBinding>>()
    val bindingKey = canonicalName + targetClassSuffix + constructorParameterTypes[0].simpleName
    var binding: Constructor<out BundleBinding>? = mBindings!![bindingKey]
    if (binding != null) {
        if (mDebug) d(TAG, "HIT: Cache found in binding weak map.")
        return binding
    }
    val targetClassName = name
    if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
        if (mDebug) d(TAG, "MISS: Reached framework class. Abandoning search.")
        return null
    }
    try {
        binding = classLoader
                .loadClass(targetClassName + targetClassSuffix)
                .getConstructor(*constructorParameterTypes) as Constructor<out BundleBinding>
        if (mDebug) d(TAG, "HIT: Loaded binding class, caching in weak map.")
    } catch (e: ClassNotFoundException) {
        if (mDebug)
            d(TAG, "Not found. Trying superclass " + superclass.name)
        val targetSuperclass = superclass
        //region abstract classes binding fix
        if (constructorParameterTypes[0] == this && constructorParameterTypes[0].superclass == targetSuperclass) {
            constructorParameterTypes[0] = targetSuperclass
        }
        //endregion
        binding = targetSuperclass.findBinding(targetClassSuffix, constructorParameterTypes)
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