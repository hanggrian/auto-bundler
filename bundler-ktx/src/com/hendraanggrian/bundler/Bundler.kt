@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")

package com.hendraanggrian.bundler

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle

inline fun Fragment.bindExtras() = Bundler.bindExtras(this)

inline fun androidx.fragment.app.Fragment.bindExtras() = Bundler.bindExtras(this)

inline fun Activity.bindExtras() = Bundler.bindExtras(this)

inline fun <T> T.bindExtras(source: Intent) = Bundler.bindExtras(this, source)

inline fun <T> T.bindExtras(source: Bundle) = Bundler.bindExtras(this, source)

inline fun <reified T> extrasOf(arg: Any): Bundle = Bundler.wrapExtras(T::class.java, arg)

inline fun <reified T> extrasOf(vararg args: Any): Bundle = Bundler.wrapExtras(T::class.java, *args)

inline fun <reified T> extrasOf(args: List<Any>): Bundle = Bundler.wrapExtras(T::class.java, args)

inline fun <T> T.restoreStates(source: Bundle) = Bundler.restoreStates(this, source)

inline fun <T> T.saveStates(source: Bundle) = Bundler.saveStates(this, source)