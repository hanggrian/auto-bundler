package com.example.bundler

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.Nullable
import android.util.Log
import android.util.SparseArray
import com.hendraanggrian.bundler.Extra
import com.hendraanggrian.bundler.initExtras
import java.io.Serializable
import java.util.*

abstract class Example1BaseActivity(override val contentLayout: Int = R.layout.activity_example1) : BaseActivity() {

    @Nullable @Extra("BOOLEAN") @JvmField var BOOLEAN: Boolean = false
    @Nullable @Extra("BOOLEAN_BOXED") @JvmField var BOOLEAN_BOXED: Boolean? = null
    @Nullable @Extra("BOOLEAN_ARRAY") @JvmField var BOOLEAN_ARRAY: BooleanArray? = null
    @Nullable @Extra("BYTE") @JvmField var BYTE: Byte = 0
    @Nullable @Extra("BYTE_BOXED") @JvmField var BYTE_BOXED: Byte? = null
    @Nullable @Extra("BYTE_ARRAY") @JvmField var BYTE_ARRAY: ByteArray? = null
    @Nullable @Extra("CHAR") @JvmField var CHAR: Char = ' '
    @Nullable @Extra("CHAR_BOXED") @JvmField var CHAR_BOXED: Char? = null
    @Nullable @Extra("CHAR_ARRAY") @JvmField var CHAR_ARRAY: CharArray? = null
    @Nullable @Extra("CHARSEQUENCE") @JvmField var CHARSEQUENCE: CharSequence? = null
    @Nullable @Extra("CHARSEQUENCE_ARRAY") @JvmField var CHARSEQUENCE_ARRAY: Array<CharSequence>? = null
    @Nullable @Extra("CHARSEQUENCE_ARRAYLIST") @JvmField var CHARSEQUENCE_ARRAYLIST: ArrayList<CharSequence>? = null
    @Nullable @Extra("DOUBLE") @JvmField var DOUBLE: Double = 0.toDouble()
    @Nullable @Extra("DOUBLE_BOXED") @JvmField var DOUBLE_BOXED: Double? = null
    @Nullable @Extra("DOUBLE_ARRAY") @JvmField var DOUBLE_ARRAY: DoubleArray? = null
    @Nullable @Extra("FLOAT") @JvmField var FLOAT: Float = 0.toFloat()
    @Nullable @Extra("FLOAT_BOXED") @JvmField var FLOAT_BOXED: Float? = null
    @Nullable @Extra("FLOAT_ARRAY") @JvmField var FLOAT_ARRAY: FloatArray? = null
    @Nullable @Extra("LONG") @JvmField var LONG: Long = 0
    @Nullable @Extra("LONG_BOXED") @JvmField var LONG_BOXED: Long? = null
    @Nullable @Extra("LONG_ARRAY") @JvmField var LONG_ARRAY: LongArray? = null
    @Nullable @Extra("INT") @JvmField var INT: Int = 0
    @Nullable @Extra("INT_BOXED") @JvmField var INT_BOXED: Int? = null
    @Nullable @Extra("INT_ARRAY") @JvmField var INT_ARRAY: IntArray? = null
    @Nullable @Extra("INT_ARRAYLIST") @JvmField var INT_ARRAYLIST: ArrayList<Int>? = null
    @Nullable @Extra("PARCELABLE") @JvmField var PARCELABLE: Parcelable? = null
    @Nullable @Extra("PARCELABLE_ARRAY") @JvmField var PARCELABLE_ARRAY: Array<Parcelable>? = null
    @Nullable @Extra("PARCELABLE_ARRAYLIST") @JvmField var PARCELABLE_ARRAYLIST: ArrayList<Parcelable>? = null
    @Nullable @Extra("PARCELABLE_SPARSEARRAY") @JvmField var PARCELABLE_SPARSEARRAY: SparseArray<Parcelable>? = null
    @Nullable @Extra("SERIALIZABLE") @JvmField var SERIALIZABLE: Serializable? = null
    @Nullable @Extra("SHORT") @JvmField var SHORT: Short = 0
    @Nullable @Extra("SHORT_BOXED") @JvmField var SHORT_BOXED: Short? = null
    @Nullable @Extra("SHORT_ARRAY") @JvmField var SHORT_ARRAY: ShortArray? = null
    @Nullable @Extra("STRING") @JvmField var STRING: String? = null
    @Nullable @Extra("STRING_ARRAY") @JvmField var STRING_ARRAY: Array<String>? = null
    @Nullable @Extra("STRING_ARRAYLIST") @JvmField var STRING_ARRAYLIST: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initExtras()
        Log.d("ASD", BOOLEAN.toString())
        //Log.d("ASD", BOOLEAN_ARRAY.format());
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}