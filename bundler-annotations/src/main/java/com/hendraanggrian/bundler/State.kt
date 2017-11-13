package com.hendraanggrian.bundler

/**
 * Key in bundle key-value pair.
 * Leave it empty to use field name as the key.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class State @JvmOverloads constructor(val value: String = "")