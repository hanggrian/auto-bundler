@file:JvmMultifileClass
@file:JvmName("BundlerKt")
@file:Suppress("DEPRECATION")

package com.hendraanggrian.auto.bundler

import android.app.Activity
import android.app.Fragment
import android.os.Bundle

/**
 * Bind fields annotated with [BindExtra] from source [Activity].
 *
 * @receiver activity and also target fields' owner.
 */
fun Activity.bindExtras(): Unit =
    Bundler.bindExtras(intent ?: error("No extras found in this Activity."), this)

/**
 * Bind fields annotated with [BindExtra] from source [Fragment].
 *
 * @receiver deprecated fragment and also target fields' owner.
 */
fun Fragment.bindExtras(): Unit =
    Bundler.bindExtras(arguments ?: error("No extras found in this Fragment."), this)

/**
 * Bind fields annotated with [BindExtra] from source [androidx.fragment.app.Fragment].
 *
 * @receiver support fragment and also target fields' owner.
 */
fun androidx.fragment.app.Fragment.bindExtras(): Unit =
    Bundler.bindExtras(arguments ?: error("No extras found in this Fragment."), this)

inline fun <reified T> extrasOf(args: MutableList<*>): Bundle =
    Bundler.wrapExtras(T::class.java, args)

inline fun <reified T> extrasOf(vararg args: Any): Bundle =
    Bundler.wrapExtras(T::class.java, mutableListOf(*args))
