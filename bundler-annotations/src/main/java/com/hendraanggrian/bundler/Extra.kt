package com.hendraanggrian.bundler

/**
 * Bind extra value to field with this annotation.
 * Field cannot be private as it would be inaccessible to binding class.
 *
 * Key in bundle key-value pair.
 * Leave it empty to use field name as the key.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Extra @JvmOverloads constructor(val value: String = "")