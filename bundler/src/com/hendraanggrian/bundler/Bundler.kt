package com.hendraanggrian.bundler

import com.hendraanggrian.bundler.internal.BundleBinding
import java.lang.reflect.Constructor

object Bundler {

    internal const val TAG = "Bundler"

    internal var mBindings: MutableMap<String, Constructor<BundleBinding>?>? = null
    internal var mDebug: Boolean = false

    fun setDebug(debug: Boolean) {
        mDebug = debug
    }
}