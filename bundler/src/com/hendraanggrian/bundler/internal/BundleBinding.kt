package com.hendraanggrian.bundler.internal

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import java.io.Serializable

abstract class BundleBinding private constructor(val source: Bundle, private val args: MutableList<*>) {

    companion object {
        val EMPTY: BundleBinding = object : BundleBinding(Bundle.EMPTY) {}
    }

    protected constructor(source: Bundle) : this(source, mutableListOf<Any>())
    protected constructor(args: MutableList<*>) : this(Bundle(), args)

    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean = source.getBoolean(key, defaultValue)
    protected fun getBooleanArray(key: String, defaultValue: BooleanArray): BooleanArray =
        findValue(key, defaultValue) { source.getBooleanArray(key) }

    protected fun getByte(key: String, defaultValue: Byte): Byte = source.getByte(key, defaultValue)
    protected fun getByteArray(key: String, defaultValue: ByteArray): ByteArray =
        findValue(key, defaultValue) { source.getByteArray(key) }

    protected fun getChar(key: String, defaultValue: Char): Char = source.getChar(key, defaultValue)
    protected fun getCharArray(key: String, defaultValue: CharArray): CharArray =
        findValue(key, defaultValue) { source.getCharArray(key) }

    protected fun getDouble(key: String, defaultValue: Double): Double = source.getDouble(key, defaultValue)
    protected fun getDoubleArray(key: String, defaultValue: DoubleArray): DoubleArray =
        findValue(key, defaultValue) { source.getDoubleArray(key) }

    protected fun getFloat(key: String, defaultValue: Float): Float = source.getFloat(key, defaultValue)
    protected fun getFloatArray(key: String, defaultValue: FloatArray): FloatArray =
        findValue(key, defaultValue) { source.getFloatArray(key) }

    protected fun getInt(key: String, defaultValue: Int): Int = source.getInt(key, defaultValue)
    protected fun getIntArray(key: String, defaultValue: IntArray): IntArray =
        findValue(key, defaultValue) { source.getIntArray(key) }

    protected fun getLong(key: String, defaultValue: Long): Long = source.getLong(key, defaultValue)
    protected fun getLongArray(key: String, defaultValue: LongArray): LongArray =
        findValue(key, defaultValue) { source.getLongArray(key) }

    protected fun getShort(key: String, defaultValue: Short): Short = source.getShort(key, defaultValue)
    protected fun getShortArray(key: String, defaultValue: ShortArray): ShortArray =
        findValue(key, defaultValue) { source.getShortArray(key) }

    protected fun getCharSequence(key: String, defaultValue: CharSequence): CharSequence =
        source.getCharSequence(key, defaultValue)

    protected fun getCharSequenceArray(key: String, defaultValue: Array<CharSequence>): Array<CharSequence> =
        findValue(key, defaultValue) { source.getCharSequenceArray(key) }

    protected fun getCharSequenceArrayList(
        key: String,
        defaultValue: ArrayList<CharSequence>
    ): ArrayList<CharSequence> = findValue(key, defaultValue) { source.getCharSequenceArrayList(key) }

    protected fun <T : Parcelable> getParcelable(key: String, defaultValue: T): T =
        findValue(key, defaultValue) { source.getParcelable(key) }

    protected fun <T : Parcelable> getParcelableArray(key: String, defaultValue: Array<T>): Array<T> =
        findValue(key, defaultValue) { source.getParcelableArray(key) as Array<T> }

    protected fun <T : Parcelable> getParcelableArrayList(key: String, defaultValue: ArrayList<T>): ArrayList<T> =
        findValue(key, defaultValue) { source.getParcelableArrayList(key) }

    protected fun <T : Parcelable> getSparseParcelableArray(key: String, defaultValue: SparseArray<T>): SparseArray<T> =
        findValue(key, defaultValue) { source.getSparseParcelableArray(key) }

    protected fun getString(key: String, defaultValue: String): String = source.getString(key, defaultValue)
    protected fun getStringArray(key: String, defaultValue: Array<String>): Array<String> =
        findValue(key, defaultValue) { source.getStringArray(key) }

    protected fun getStringArrayList(key: String, defaultValue: ArrayList<String>): ArrayList<String> =
        findValue(key, defaultValue) { source.getStringArrayList(key) }

    protected fun <T : Serializable> getSerializable(key: String, defaultValue: T): T =
        findValue(key, defaultValue) { source.getSerializable(key) as T }

    protected fun checkRequired(key: String, targetName: String) {
        check(source.containsKey(key)) {
            error(
                "Required extra `$targetName` with key `$key` not found, " +
                    "if this extra is optional add @Nullable to this field."
            )
        }
    }

    protected fun nextArg(): Any? {
        val arg = args[0]
        args.removeAt(0)
        return arg
    }

    private inline fun <T> findValue(key: String, defaultValue: T, getValue: (key: String) -> T?): T {
        if (source.containsKey(key)) {
            val value = getValue(key)
            if (value != null) {
                return value
            }
        }
        return defaultValue
    }
}
