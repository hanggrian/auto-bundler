@file:JvmMultifileClass
@file:JvmName("BundlesKt")
@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package com.hendraanggrian.auto.bundles

import android.app.Activity
import android.app.Fragment
import android.os.Bundle

inline fun Activity.restoreStates(source: Bundle): Unit = Bundles.restoreStates(source, this)

inline fun Fragment.restoreStates(source: Bundle): Unit = Bundles.restoreStates(source, this)

inline fun androidx.fragment.app.Fragment.restoreStates(source: Bundle): Unit = Bundles.restoreStates(source, this)

inline fun Activity.saveStates(source: Bundle): Bundle = Bundles.saveStates(source, this)

inline fun Fragment.saveStates(source: Bundle): Bundle = Bundles.saveStates(source, this)

inline fun androidx.fragment.app.Fragment.saveStates(source: Bundle): Bundle = Bundles.saveStates(source, this)
