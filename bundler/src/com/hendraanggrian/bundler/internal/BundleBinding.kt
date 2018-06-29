package com.hendraanggrian.bundler.internal

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.util.SparseArray
import java.io.Serializable
import java.util.Collections.EMPTY_LIST

/** Superclass of all generated bundle bindings. */
abstract class BundleBinding private constructor(
    @JvmField protected val source: Bundle,
    private val args: MutableList<*>
) {

    protected constructor(source: Bundle) : this(source, EMPTY_LIST)
    protected constructor(args: MutableList<*>) : this(Bundle(), args)

    internal val bundle: Bundle get() = source

    // region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    protected fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean = source.getBoolean(key, defaultValue)

    protected fun getBoolean(
        key: String,
        defaultValue: Boolean?
    ): Boolean? = find(key, defaultValue) { getBoolean(it) }

    protected fun getBooleanArray(
        key: String,
        defaultValue: BooleanArray?
    ): BooleanArray? = find(key, defaultValue) { getBooleanArray(it) }

    protected fun getByte(
        key: String,
        defaultValue: Byte
    ): Byte = source.getByte(key, defaultValue)!!

    protected fun getByte(
        key: String,
        defaultValue: Byte?
    ): Byte? = find(key, defaultValue) { getByte(it) }

    protected fun getByteArray(
        key: String,
        defaultValue: ByteArray?
    ): ByteArray? = find(key, defaultValue) { getByteArray(it) }

    protected fun getChar(
        key: String,
        defaultValue: Char
    ): Char = source.getChar(key, defaultValue)

    protected fun getChar(
        key: String,
        defaultValue: Char?
    ): Char? = find(key, defaultValue) { getChar(it) }

    protected fun getCharArray(
        key: String,
        defaultValue: CharArray?
    ): CharArray? = find(key, defaultValue) { getCharArray(it) }

    protected fun getDouble(
        key: String,
        defaultValue: Double
    ): Double = source.getDouble(key, defaultValue)

    protected fun getDouble(
        key: String,
        defaultValue: Double?
    ): Double? = find(key, defaultValue) { getDouble(it) }

    protected fun getDoubleArray(
        key: String,
        defaultValue: DoubleArray?
    ): DoubleArray? = find(key, defaultValue) { getDoubleArray(it) }

    protected fun getFloat(
        key: String,
        defaultValue: Float
    ): Float = source.getFloat(key, defaultValue)

    protected fun getFloat(
        key: String,
        defaultValue: Float?
    ): Float? = find(key, defaultValue) { getFloat(it) }

    protected fun getFloatArray(
        key: String,
        defaultValue: FloatArray?
    ): FloatArray? = find(key, defaultValue) { getFloatArray(it) }

    protected fun getInt(
        key: String,
        defaultValue: Int
    ): Int = source.getInt(key, defaultValue)

    protected fun getInt(
        key: String,
        defaultValue: Int?
    ): Int? = find(key, defaultValue) { getInt(it) }

    protected fun getIntArray(
        key: String,
        defaultValue: IntArray?
    ): IntArray? = find(key, defaultValue) { getIntArray(it) }

    protected fun getIntegerArrayList(
        key: String,
        defaultValue: ArrayList<Int>?
    ): ArrayList<Int>? = find(key, defaultValue) { getIntegerArrayList(it) }

    protected fun getLong(
        key: String,
        defaultValue: Long
    ): Long = source.getLong(key, defaultValue)

    protected fun getLong(
        key: String,
        defaultValue: Long?
    ): Long? = find(key, defaultValue) { getLong(key) }

    protected fun getLongArray(
        key: String,
        defaultValue: LongArray?
    ): LongArray? = find(key, defaultValue) { getLongArray(it) }

    protected fun getShort(
        key: String,
        defaultValue: Short
    ): Short = source.getShort(key, defaultValue)

    protected fun getShort(
        key: String,
        defaultValue: Short?
    ): Short? = find(key, defaultValue) { getShort(it) }

    protected fun getShortArray(
        key: String,
        defaultValue: ShortArray?
    ): ShortArray? = find(key, defaultValue) { getShortArray(it) }
    // endregion

    // region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    @RequiresApi(12)
    protected fun getCharSequence(
        key: String,
        defaultValue: CharSequence?
    ): CharSequence? = source.getCharSequence(key, defaultValue)

    protected fun getCharSequenceArray(
        key: String,
        defaultValue: Array<CharSequence>?
    ): Array<CharSequence>? = find(key, defaultValue) { getCharSequenceArray(it) }

    protected fun getCharSequenceArrayList(
        key: String,
        defaultValue: ArrayList<CharSequence>?
    ): ArrayList<CharSequence>? = find(key, defaultValue) { getCharSequenceArrayList(it) }

    protected fun <T : Parcelable> getParcelable(
        key: String,
        defaultValue: T?
    ): T? = find(key, defaultValue) { getParcelable(it) }

    protected fun getParcelableArray(
        key: String,
        defaultValue: Array<Parcelable>?
    ): Array<Parcelable>? = find(key, defaultValue) { getParcelableArray(it) }

    protected fun <T : Parcelable> getParcelableArrayList(
        key: String,
        defaultValue: ArrayList<T>?
    ): ArrayList<T>? = find(key, defaultValue) { getParcelableArrayList(it) }

    protected fun <T : Parcelable> getSparseParcelableArray(
        key: String,
        defaultValue: SparseArray<T>?
    ): SparseArray<T>? = find(key, defaultValue) { getSparseParcelableArray(it) }

    @RequiresApi(12)
    protected fun getString(
        key: String,
        defaultValue: String?
    ): String? = source.getString(key, defaultValue)

    protected fun getStringArray(
        key: String,
        defaultValue: Array<String>?
    ): Array<String>? = find(key, defaultValue) { getStringArray(it) }

    protected fun getStringArrayList(
        key: String,
        defaultValue: ArrayList<String>?
    ): ArrayList<String>? = find(key, defaultValue) { getStringArrayList(it) }
    // endregion

    // region Others: Parceler and Serializable.
    protected fun getSerializable(
        key: String,
        defaultValue: Serializable?
    ): Serializable? = find(key, defaultValue) { getSerializable(it) }
    // endregion

    protected fun checkRequired(key: String, targetName: String) {
        if (!source.containsKey(key)) {
            error("Required extra $targetName with key $key not found, " +
                "if this extra is optional add @Nullable to this field.")
        }
    }

    protected fun nextArg(): Any? {
        val arg = args[0]
        args.removeAt(0)
        return arg
    }

    private inline fun <T> find(
        key: String,
        defaultValue: T,
        getter: Bundle.(String) -> T
    ): T = when {
        source.containsKey(key) -> source.getter(key)
        else -> defaultValue
    }

    companion object {

        internal val EMPTY: BundleBinding = object : BundleBinding(Bundle.EMPTY) {}
    }
}