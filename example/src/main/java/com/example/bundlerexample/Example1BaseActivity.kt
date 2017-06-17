package com.example.bundlerexample

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.Nullable
import android.util.Log
import android.util.SparseArray
import com.hendraanggrian.bundler.BindExtra
import com.hendraanggrian.bundler.Bundler
import java.io.Serializable
import java.util.*

abstract class Example1BaseActivity(override val contentLayout: Int = R.layout.activity_example1) : BaseActivity() {

    @Nullable @BindExtra("BOOLEAN") @JvmField var BOOLEAN: Boolean = false
    @Nullable @BindExtra("BOOLEAN_BOXED") @JvmField var BOOLEAN_BOXED: Boolean? = null
    @Nullable @BindExtra("BOOLEAN_ARRAY") @JvmField var BOOLEAN_ARRAY: BooleanArray? = null
    @Nullable @BindExtra("BYTE") @JvmField var BYTE: Byte = 0
    @Nullable @BindExtra("BYTE_BOXED") @JvmField var BYTE_BOXED: Byte? = null
    @Nullable @BindExtra("BYTE_ARRAY") @JvmField var BYTE_ARRAY: ByteArray? = null
    @Nullable @BindExtra("CHAR") @JvmField var CHAR: Char = ' '
    @Nullable @BindExtra("CHAR_BOXED") @JvmField var CHAR_BOXED: Char? = null
    @Nullable @BindExtra("CHAR_ARRAY") @JvmField var CHAR_ARRAY: CharArray? = null
    @Nullable @BindExtra("CHARSEQUENCE") @JvmField var CHARSEQUENCE: CharSequence? = null
    @Nullable @BindExtra("CHARSEQUENCE_ARRAY") @JvmField var CHARSEQUENCE_ARRAY: Array<CharSequence>? = null
    @Nullable @BindExtra("CHARSEQUENCE_ARRAYLIST") @JvmField var CHARSEQUENCE_ARRAYLIST: ArrayList<CharSequence>? = null
    @Nullable @BindExtra("DOUBLE") @JvmField var DOUBLE: Double = 0.toDouble()
    @Nullable @BindExtra("DOUBLE_BOXED") @JvmField var DOUBLE_BOXED: Double? = null
    @Nullable @BindExtra("DOUBLE_ARRAY") @JvmField var DOUBLE_ARRAY: DoubleArray? = null
    @Nullable @BindExtra("FLOAT") @JvmField var FLOAT: Float = 0.toFloat()
    @Nullable @BindExtra("FLOAT_BOXED") @JvmField var FLOAT_BOXED: Float? = null
    @Nullable @BindExtra("FLOAT_ARRAY") @JvmField var FLOAT_ARRAY: FloatArray? = null
    @Nullable @BindExtra("LONG") @JvmField var LONG: Long = 0
    @Nullable @BindExtra("LONG_BOXED") @JvmField var LONG_BOXED: Long? = null
    @Nullable @BindExtra("LONG_ARRAY") @JvmField var LONG_ARRAY: LongArray? = null
    @Nullable @BindExtra("INT") @JvmField var INT: Int = 0
    @Nullable @BindExtra("INT_BOXED") @JvmField var INT_BOXED: Int? = null
    @Nullable @BindExtra("INT_ARRAY") @JvmField var INT_ARRAY: IntArray? = null
    @Nullable @BindExtra("INT_ARRAYLIST") @JvmField var INT_ARRAYLIST: ArrayList<Int>? = null
    @Nullable @BindExtra("PARCELABLE") @JvmField var PARCELABLE: Parcelable? = null
    @Nullable @BindExtra("PARCELABLE_ARRAY") @JvmField var PARCELABLE_ARRAY: Array<Parcelable>? = null
    @Nullable @BindExtra("PARCELABLE_ARRAYLIST") @JvmField var PARCELABLE_ARRAYLIST: ArrayList<Parcelable>? = null
    @Nullable @BindExtra("PARCELABLE_SPARSEARRAY") @JvmField var PARCELABLE_SPARSEARRAY: SparseArray<Parcelable>? = null
    @Nullable @BindExtra("SERIALIZABLE") @JvmField var SERIALIZABLE: Serializable? = null
    @Nullable @BindExtra("SHORT") @JvmField var SHORT: Short = 0
    @Nullable @BindExtra("SHORT_BOXED") @JvmField var SHORT_BOXED: Short? = null
    @Nullable @BindExtra("SHORT_ARRAY") @JvmField var SHORT_ARRAY: ShortArray? = null
    @Nullable @BindExtra("STRING") @JvmField var STRING: String? = null
    @Nullable @BindExtra("STRING_ARRAY") @JvmField var STRING_ARRAY: Array<String>? = null
    @Nullable @BindExtra("STRING_ARRAYLIST") @JvmField var STRING_ARRAYLIST: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bundler.bindExtras(this)
        Log.d("ASD", BOOLEAN.toString())
        //Log.d("ASD", BOOLEAN_ARRAY.format());
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}