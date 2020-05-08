package com.hendraanggrian.bundler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hendraanggrian.bundler.internal.BundleBinding
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.WeakHashMap
import org.json.JSONObject

object Bundler {
    private const val TAG = "Bundler"
    private var BINDINGS: MutableMap<String, Constructor<BundleBinding>>? = null
    private var DEBUG = false

    /** When set to true, will print Bundler operation in debug level. */
    fun setDebug(debug: Boolean) {
        DEBUG = debug
    }

    internal fun debug(message: String) {
        if (DEBUG) {
            Log.d(TAG, message)
        }
    }

    internal fun debug(message: Bundle) {
        if (DEBUG) {
            try {
                val jsonObject = JSONObject()
                for (key in message.keySet()) {
                    jsonObject.put(key, message[key])
                }
                val json = jsonObject.toString(4)
                val split = mutableListOf(*json.split("\\r?\\n").toTypedArray())
                val lastIndex = split.size - 1
                if (split[lastIndex].isEmpty()) {
                    split.removeAt(lastIndex)
                }
                split.forEach(::debug)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Bind fields annotated with [BindExtra] from source [Intent].
     * @param source intent to extract.
     * @param target fields' owner.
     * @throws RuntimeException when constructor of binding class cannot be found.
     */
    fun bindExtras(source: Intent, target: Any) =
        bindExtras(source.extras ?: error("No extras found in this Intent."), target)

    /**
     * Bind fields annotated with [BindExtra] from source [Bundle].
     * @param source bundle to extract.
     * @param target fields' owner.
     * @throws RuntimeException when constructor of binding class cannot be found.
     */
    fun bindExtras(source: Bundle, target: Any) {
        val binding = createBinding(
            target.javaClass, BindExtra.SUFFIX,
            arrayOf(Bundle::class.java, target.javaClass),
            arrayOf(source, target)
        )
        debug(binding.source)
    }

    fun wrapExtras(targetCls: Class<*>, args: List<*>): Bundle {
        val binding = createBinding(
            targetCls, BindExtra.SUFFIX,
            arrayOf(MutableList::class.java),
            arrayOf(args)
        )
        val extras = binding.source
        debug(extras)
        return extras
    }

    fun restoreStates(source: Bundle, target: Any) {
        val binding = createBinding(
            target.javaClass, BindState.SUFFIX,
            arrayOf(Bundle::class.java, target.javaClass),
            arrayOf(source, target)
        )
        debug(binding.source)
    }

    fun saveStates(source: Bundle, target: Any): Bundle {
        val binding = createBinding(
            target.javaClass, BindState.SUFFIX,
            arrayOf(target.javaClass, Bundle::class.java),
            arrayOf(target, source)
        )
        val states = binding.source
        source.putAll(states)
        debug(states)
        return source
    }

    private fun createBinding(cls: Class<*>, clsSuffix: String, paramTypes: Array<Class<*>>, params: Array<*>):
        BundleBinding {
        val clsName = cls.simpleName
        debug("Looking up constructor for $clsName")
        val constructor = findBindingConstructor(cls, clsSuffix, paramTypes)
        if (constructor == null) {
            debug("Ignored because no constructor was found in $clsName")
            return BundleBinding.EMPTY
        }
        return try {
            constructor.newInstance(*params)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to invoke $constructor", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to invoke $constructor", e)
        } catch (e: InvocationTargetException) {
            when (val cause = e.cause) {
                is RuntimeException -> throw cause
                is Error -> throw cause
                else -> throw RuntimeException("Unable to create binding instance.", cause)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun findBindingConstructor(cls: Class<*>, clsSuffix: String, paramTypes: Array<Class<*>>):
        Constructor<BundleBinding>? {
        if (BINDINGS == null) {
            BINDINGS = WeakHashMap()
        }
        val bindingKey = cls.canonicalName + clsSuffix + paramTypes[0].simpleName
        var binding = BINDINGS!![bindingKey]
        if (binding != null) {
            debug("HIT: Cache found in binding weak map.")
            return binding
        }
        val clsName = cls.simpleName
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            debug("MISS: Reached framework class. Abandoning search.")
            return null
        }
        try {
            binding = cls.classLoader!!
                .loadClass(clsName + clsSuffix)
                .getConstructor(*paramTypes) as Constructor<BundleBinding>
            debug("HIT: Loaded binding class, caching in weak map.")
        } catch (e: ClassNotFoundException) {
            val supercls = cls.superclass!!
            debug("Not found. Trying superclass ${supercls.simpleName}")
            // abstract classes binding fix
            if (paramTypes[0] == cls && paramTypes[0].superclass == supercls) {
                paramTypes[0] = supercls
            }
            binding = findBindingConstructor(supercls, clsSuffix, paramTypes)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Unable to find binding constructor for $clsName", e)
        }
        BINDINGS!![bindingKey] = checkNotNull(binding) {
            "Unable to find preferences binding, is `bundler-compiler` correctly installed?"
        }
        return binding
    }
}
