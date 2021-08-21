@file:JvmMultifileClass
@file:JvmName("BundlesKt")
@file:Suppress("DEPRECATION")

package com.hendraanggrian.auto.bundles

import android.app.Activity
import android.app.Fragment
import android.os.Bundle

/**
 * Bind fields annotated with [BindExtra] from source [Activity].
 * @receiver activity and also target fields' owner.
 */
fun Activity.bindExtras(): Unit =
    Bundles.bindExtras(intent ?: error("No extras found in this Activity."), this)

/**
 * Bind fields annotated with [BindExtra] from source [Fragment].
 * @receiver deprecated fragment and also target fields' owner.
 */
fun Fragment.bindExtras(): Unit =
    Bundles.bindExtras(arguments ?: error("No extras found in this Fragment."), this)

/**
 * Bind fields annotated with [BindExtra] from source [androidx.fragment.app.Fragment].
 * @receiver support fragment and also target fields' owner.
 */
fun androidx.fragment.app.Fragment.bindExtras(): Unit =
    Bundles.bindExtras(arguments ?: error("No extras found in this Fragment."), this)

inline fun <reified T> extrasOf(args: MutableList<*>): Bundle =
    Bundles.wrapExtras(T::class.java, args)

inline fun <reified T> extrasOf(vararg args: Any): Bundle =
    Bundles.wrapExtras(T::class.java, mutableListOf(*args))
