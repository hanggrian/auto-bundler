package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class ExtraBinding extends Binding {

    protected ExtraBinding(@NonNull Bundle source) {
        super(source);
    }

    //region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return source.getBoolean(key, defaultValue);
    }

    @Nullable
    public Boolean getBoolean(@NonNull String key, @Nullable Boolean defaultValue) {
        if (source.containsKey(key))
            return source.getBoolean(key);
        return defaultValue;
    }

    @Nullable
    public boolean[] getBooleanArray(@NonNull String key, @Nullable boolean[] defaultValue) {
        if (source.containsKey(key))
            return source.getBooleanArray(key);
        return defaultValue;
    }

    public byte getByte(@NonNull String key, byte defaultValue) {
        return source.getByte(key, defaultValue);
    }

    @Nullable
    public Byte getByte(@NonNull String key, @Nullable Byte defaultValue) {
        if (source.containsKey(key))
            return source.getByte(key);
        return defaultValue;
    }

    @Nullable
    public byte[] getByteArray(@NonNull String key, @Nullable byte[] defaultValue) {
        if (source.containsKey(key))
            return source.getByteArray(key);
        return defaultValue;
    }

    public char getChar(@NonNull String key, char defaultValue) {
        return source.getChar(key, defaultValue);
    }

    @Nullable
    public Character getChar(@NonNull String key, @Nullable Character defaultValue) {
        if (source.containsKey(key))
            return source.getChar(key);
        return defaultValue;
    }

    @Nullable
    public char[] getCharArray(@NonNull String key, @Nullable char[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharArray(key);
        return defaultValue;
    }

    public double getDouble(@NonNull String key, double defaultValue) {
        return source.getDouble(key, defaultValue);
    }

    @Nullable
    public Double getDouble(@NonNull String key, @Nullable Double defaultValue) {
        if (source.containsKey(key))
            return source.getDouble(key);
        return defaultValue;
    }

    @Nullable
    public double[] getDoubleArray(@NonNull String key, @Nullable double[] defaultValue) {
        if (source.containsKey(key))
            return source.getDoubleArray(key);
        return defaultValue;
    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return source.getFloat(key, defaultValue);
    }

    @Nullable
    public Float getFloat(@NonNull String key, @Nullable Float defaultValue) {
        if (source.containsKey(key))
            return source.getFloat(key);
        return defaultValue;
    }

    @Nullable
    public float[] getFloatArray(@NonNull String key, @Nullable float[] defaultValue) {
        if (source.containsKey(key))
            return source.getFloatArray(key);
        return defaultValue;
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return source.getInt(key, defaultValue);
    }

    @Nullable
    public Integer getInt(@NonNull String key, @Nullable Integer defaultValue) {
        if (source.containsKey(key))
            return source.getInt(key);
        return defaultValue;
    }

    @Nullable
    public int[] getIntArray(@NonNull String key, @Nullable int[] defaultValue) {
        if (source.containsKey(key))
            return source.getIntArray(key);
        return defaultValue;
    }

    @Nullable
    public ArrayList<Integer> getIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> defaultValue) {
        if (source.containsKey(key))
            return source.getIntegerArrayList(key);
        return defaultValue;
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return source.getLong(key, defaultValue);
    }

    @Nullable
    public Long getLong(@NonNull String key, @Nullable Long defaultValue) {
        if (source.containsKey(key))
            return source.getLong(key);
        return defaultValue;
    }

    @Nullable
    public long[] getLongArray(@NonNull String key, @Nullable long[] defaultValue) {
        if (source.containsKey(key))
            return source.getLongArray(key);
        return defaultValue;
    }

    public short getShort(@NonNull String key, short defaultValue) {
        return source.getShort(key, defaultValue);
    }

    @Nullable
    public Short getShort(@NonNull String key, @Nullable Short defaultValue) {
        if (source.containsKey(key))
            return source.getShort(key);
        return defaultValue;
    }

    @Nullable
    public short[] getShortArray(@NonNull String key, @Nullable short[] defaultValue) {
        if (source.containsKey(key))
            return source.getShortArray(key);
        return defaultValue;
    }
    //endregion

    //region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    @Nullable
    public CharSequence getCharSequence(@NonNull String key, @Nullable CharSequence defaultValue) {
        return source.getCharSequence(key, defaultValue);
    }

    @Nullable
    public CharSequence[] getCharSequenceArray(@NonNull String key, @Nullable CharSequence[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArray(key);
        return defaultValue;
    }

    @Nullable
    public ArrayList<CharSequence> getCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArrayList(key);
        return defaultValue;
    }

    @Nullable
    public <T extends Parcelable> T getParcelable(@NonNull String key, @Nullable T defaultValue) {
        if (source.containsKey(key))
            return source.getParcelable(key);
        return defaultValue;
    }

    @Nullable
    public Parcelable[] getParcelableArray(@NonNull String key, @Nullable Parcelable[] defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    public <T extends Parcelable> ArrayList<T> getParcelableArrayList(@NonNull String key, @Nullable ArrayList<T> defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArrayList(key);
        return defaultValue;
    }

    @Nullable
    public <T extends Parcelable> SparseArray<T> getSparseParcelableArray(@NonNull String key, @Nullable SparseArray<T> defaultValue) {
        if (source.containsKey(key))
            return source.getSparseParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    public String getString(@NonNull String key, @Nullable String defaultValue) {
        return source.getString(key, defaultValue);
    }

    @Nullable
    public String[] getStringArray(@NonNull String key, @Nullable String[] defaultValue) {
        if (source.containsKey(key))
            return source.getStringArray(key);
        return defaultValue;
    }

    @Nullable
    public ArrayList<String> getStringArrayList(@NonNull String key, @Nullable ArrayList<String> defaultValue) {
        if (source.containsKey(key))
            return source.getStringArrayList(key);
        return defaultValue;
    }
    //endregion

    //region Others: Parceler and Serializable.
    @Nullable
    public <T> T getParcelable(@NonNull String key, @Nullable T defaultValue) {
        if (source.containsKey(key))
            return Parcels.unwrap(source.getParcelable(key));
        return defaultValue;
    }

    @Nullable
    public Serializable getSerializable(@NonNull String key, @Nullable Serializable defaultValue) {
        if (source.containsKey(key))
            source.getSerializable(key);
        return defaultValue;
    }
    //endregion

    public void checkRequired(@NonNull String key, @NonNull String targetName) {
        if (!source.containsKey(key))
            throw new IllegalStateException(String.format("Required extra '%s' wrap key '%s' not found, if this extra is optional add @Nullable to this field.", targetName, key));
    }
}