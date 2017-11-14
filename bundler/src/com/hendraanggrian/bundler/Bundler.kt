package com.hendraanggrian.bundler

import android.util.Log.DEBUG
import com.hendraanggrian.bundler.internal.BundleBinding
import java.lang.reflect.Constructor

object Bundler {

    internal const val TAG = "Bundler"

    internal var mBindings: MutableMap<String, Constructor<BundleBinding>?>? = null
    internal var mDebug: Boolean = false

    /** When set to true, will print Bundler operation in [DEBUG] level. */
    fun setDebug(debug: Boolean) {
        mDebug = debug
    }
}