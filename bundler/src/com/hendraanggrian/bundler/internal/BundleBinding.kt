package com.hendraanggrian.bundler.internal

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.util.SparseArray
import java.io.Serializable
import java.util.*

/** Superclass of all generated bundle bindings. */
abstract class BundleBinding private constructor(protected val source: Bundle, private val args: MutableList<*>) {

    protected constructor(source: Bundle) : this(source, Collections.EMPTY_LIST)
    protected constructor(args: MutableList<*>) : this(Bundle(), args)

    internal val mSource: Bundle get() = source

    //region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean = source.getBoolean(key, defaultValue)

    protected fun getBoolean(key: String, defaultValue: Boolean?): Boolean? = if (source.containsKey(key)) source.getBoolean(key) else defaultValue

    protected fun getBooleanArray(key: String, defaultValue: BooleanArray?): BooleanArray? = if (source.containsKey(key)) source.getBooleanArray(key) else defaultValue

    protected fun getByte(key: String, defaultValue: Byte): Byte = source.getByte(key, defaultValue)!!

    protected fun getByte(key: String, defaultValue: Byte?): Byte? = if (source.containsKey(key)) source.getByte(key) else defaultValue

    protected fun getByteArray(key: String, defaultValue: ByteArray?): ByteArray? = if (source.containsKey(key)) source.getByteArray(key) else defaultValue

    protected fun getChar(key: String, defaultValue: Char): Char = source.getChar(key, defaultValue)

    protected fun getChar(key: String, defaultValue: Char?): Char? = if (source.containsKey(key)) source.getChar(key) else defaultValue

    protected fun getCharArray(key: String, defaultValue: CharArray?): CharArray? = if (source.containsKey(key)) source.getCharArray(key) else defaultValue

    protected fun getDouble(key: String, defaultValue: Double): Double = source.getDouble(key, defaultValue)

    protected fun getDouble(key: String, defaultValue: Double?): Double? = if (source.containsKey(key)) source.getDouble(key) else defaultValue

    protected fun getDoubleArray(key: String, defaultValue: DoubleArray?): DoubleArray? = if (source.containsKey(key)) source.getDoubleArray(key) else defaultValue

    protected fun getFloat(key: String, defaultValue: Float): Float = source.getFloat(key, defaultValue)

    protected fun getFloat(key: String, defaultValue: Float?): Float? = if (source.containsKey(key)) source.getFloat(key) else defaultValue

    protected fun getFloatArray(key: String, defaultValue: FloatArray?): FloatArray? = if (source.containsKey(key)) source.getFloatArray(key) else defaultValue

    protected fun getInt(key: String, defaultValue: Int): Int = source.getInt(key, defaultValue)

    protected fun getInt(key: String, defaultValue: Int?): Int? = if (source.containsKey(key)) source.getInt(key) else defaultValue

    protected fun getIntArray(key: String, defaultValue: IntArray?): IntArray? = if (source.containsKey(key)) source.getIntArray(key) else defaultValue

    protected fun getIntegerArrayList(key: String, defaultValue: ArrayList<Int>?): ArrayList<Int>? = if (source.containsKey(key)) source.getIntegerArrayList(key) else defaultValue

    protected fun getLong(key: String, defaultValue: Long): Long = source.getLong(key, defaultValue)

    protected fun getLong(key: String, defaultValue: Long?): Long? = if (source.containsKey(key)) source.getLong(key) else defaultValue

    protected fun getLongArray(key: String, defaultValue: LongArray?): LongArray? = if (source.containsKey(key)) source.getLongArray(key) else defaultValue

    protected fun getShort(key: String, defaultValue: Short): Short = source.getShort(key, defaultValue)

    protected fun getShort(key: String, defaultValue: Short?): Short? = if (source.containsKey(key)) source.getShort(key) else defaultValue

    protected fun getShortArray(key: String, defaultValue: ShortArray?): ShortArray? = if (source.containsKey(key)) source.getShortArray(key) else defaultValue
    //endregion

    //region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    @RequiresApi(12)
    protected fun getCharSequence(key: String, defaultValue: CharSequence?): CharSequence? = source.getCharSequence(key, defaultValue)

    protected fun getCharSequenceArray(key: String, defaultValue: Array<CharSequence>?): Array<CharSequence>? = if (source.containsKey(key)) source.getCharSequenceArray(key) else defaultValue

    protected fun getCharSequenceArrayList(key: String, defaultValue: ArrayList<CharSequence>?): ArrayList<CharSequence>? = if (source.containsKey(key)) source.getCharSequenceArrayList(key) else defaultValue

    protected fun <T : Parcelable> getParcelable(key: String, defaultValue: T?): T? = if (source.containsKey(key)) source.getParcelable(key) else defaultValue

    protected fun getParcelableArray(key: String, defaultValue: Array<Parcelable>?): Array<Parcelable>? = if (source.containsKey(key)) source.getParcelableArray(key) else defaultValue

    protected fun <T : Parcelable> getParcelableArrayList(key: String, defaultValue: ArrayList<T>?): ArrayList<T>? = if (source.containsKey(key)) source.getParcelableArrayList(key) else defaultValue

    protected fun <T : Parcelable> getSparseParcelableArray(key: String, defaultValue: SparseArray<T>?): SparseArray<T>? = if (source.containsKey(key)) source.getSparseParcelableArray(key) else defaultValue

    @RequiresApi(12)
    protected fun getString(key: String, defaultValue: String?): String? = source.getString(key, defaultValue)

    protected fun getStringArray(key: String, defaultValue: Array<String>?): Array<String>? = if (source.containsKey(key)) source.getStringArray(key) else defaultValue

    protected fun getStringArrayList(key: String, defaultValue: ArrayList<String>?): ArrayList<String>? = if (source.containsKey(key)) source.getStringArrayList(key) else defaultValue
    //endregion

    //region Others: Parceler and Serializable.
    protected fun getSerializable(key: String, defaultValue: Serializable?): Serializable? = if (source.containsKey(key)) source.getSerializable(key) else defaultValue
    //endregion

    protected fun checkRequired(key: String, targetName: String) {
        if (!source.containsKey(key)) error("Required extra $targetName with key $key not found, if this extra is optional add @Nullable to this field.")
    }

    protected fun nextArg(): Any? {
        val arg = args[0]
        args.removeAt(0)
        return arg
    }

    companion object {
        internal val EMPTY: BundleBinding = object : BundleBinding(Bundle.EMPTY) {}
    }
}