package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class BundleBinding {

    static final BundleBinding EMPTY;

    static {
        EMPTY = new BundleBinding(Bundle.EMPTY) {
        };
    }

    @NonNull protected final Bundle source;
    @NonNull private final List args;

    protected BundleBinding(@NonNull Bundle source) {
        this(source, Collections.EMPTY_LIST);
    }

    protected BundleBinding(@NonNull List args) {
        this(new Bundle(), args);
    }

    private BundleBinding(@NonNull Bundle source, @NonNull List args) {
        this.source = source;
        this.args = args;
    }

    //region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    protected boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return source.getBoolean(key, defaultValue);
    }

    @Nullable
    protected Boolean getBoolean(@NonNull String key, @Nullable Boolean defaultValue) {
        if (source.containsKey(key))
            return source.getBoolean(key);
        return defaultValue;
    }

    @Nullable
    protected boolean[] getBooleanArray(@NonNull String key, @Nullable boolean[] defaultValue) {
        if (source.containsKey(key))
            return source.getBooleanArray(key);
        return defaultValue;
    }

    protected byte getByte(@NonNull String key, byte defaultValue) {
        return source.getByte(key, defaultValue);
    }

    @Nullable
    protected Byte getByte(@NonNull String key, @Nullable Byte defaultValue) {
        if (source.containsKey(key))
            return source.getByte(key);
        return defaultValue;
    }

    @Nullable
    protected byte[] getByteArray(@NonNull String key, @Nullable byte[] defaultValue) {
        if (source.containsKey(key))
            return source.getByteArray(key);
        return defaultValue;
    }

    protected char getChar(@NonNull String key, char defaultValue) {
        return source.getChar(key, defaultValue);
    }

    @Nullable
    protected Character getChar(@NonNull String key, @Nullable Character defaultValue) {
        if (source.containsKey(key))
            return source.getChar(key);
        return defaultValue;
    }

    @Nullable
    protected char[] getCharArray(@NonNull String key, @Nullable char[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharArray(key);
        return defaultValue;
    }

    protected double getDouble(@NonNull String key, double defaultValue) {
        return source.getDouble(key, defaultValue);
    }

    @Nullable
    protected Double getDouble(@NonNull String key, @Nullable Double defaultValue) {
        if (source.containsKey(key))
            return source.getDouble(key);
        return defaultValue;
    }

    @Nullable
    protected double[] getDoubleArray(@NonNull String key, @Nullable double[] defaultValue) {
        if (source.containsKey(key))
            return source.getDoubleArray(key);
        return defaultValue;
    }

    protected float getFloat(@NonNull String key, float defaultValue) {
        return source.getFloat(key, defaultValue);
    }

    @Nullable
    protected Float getFloat(@NonNull String key, @Nullable Float defaultValue) {
        if (source.containsKey(key))
            return source.getFloat(key);
        return defaultValue;
    }

    @Nullable
    protected float[] getFloatArray(@NonNull String key, @Nullable float[] defaultValue) {
        if (source.containsKey(key))
            return source.getFloatArray(key);
        return defaultValue;
    }

    protected int getInt(@NonNull String key, int defaultValue) {
        return source.getInt(key, defaultValue);
    }

    @Nullable
    protected Integer getInt(@NonNull String key, @Nullable Integer defaultValue) {
        if (source.containsKey(key))
            return source.getInt(key);
        return defaultValue;
    }

    @Nullable
    protected int[] getIntArray(@NonNull String key, @Nullable int[] defaultValue) {
        if (source.containsKey(key))
            return source.getIntArray(key);
        return defaultValue;
    }

    @Nullable
    protected ArrayList<Integer> getIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> defaultValue) {
        if (source.containsKey(key))
            return source.getIntegerArrayList(key);
        return defaultValue;
    }

    protected long getLong(@NonNull String key, long defaultValue) {
        return source.getLong(key, defaultValue);
    }

    @Nullable
    protected Long getLong(@NonNull String key, @Nullable Long defaultValue) {
        if (source.containsKey(key))
            return source.getLong(key);
        return defaultValue;
    }

    @Nullable
    protected long[] getLongArray(@NonNull String key, @Nullable long[] defaultValue) {
        if (source.containsKey(key))
            return source.getLongArray(key);
        return defaultValue;
    }

    protected short getShort(@NonNull String key, short defaultValue) {
        return source.getShort(key, defaultValue);
    }

    @Nullable
    protected Short getShort(@NonNull String key, @Nullable Short defaultValue) {
        if (source.containsKey(key))
            return source.getShort(key);
        return defaultValue;
    }

    @Nullable
    protected short[] getShortArray(@NonNull String key, @Nullable short[] defaultValue) {
        if (source.containsKey(key))
            return source.getShortArray(key);
        return defaultValue;
    }
    //endregion

    //region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    @Nullable
    protected CharSequence getCharSequence(@NonNull String key, @Nullable CharSequence defaultValue) {
        return source.getCharSequence(key, defaultValue);
    }

    @Nullable
    protected CharSequence[] getCharSequenceArray(@NonNull String key, @Nullable CharSequence[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArray(key);
        return defaultValue;
    }

    @Nullable
    protected ArrayList<CharSequence> getCharSequenceArrayList(@NonNull String key, @Nullable ArrayList<CharSequence> defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArrayList(key);
        return defaultValue;
    }

    @Nullable
    protected <T extends Parcelable> T getParcelable(@NonNull String key, @Nullable T defaultValue) {
        if (source.containsKey(key))
            return source.getParcelable(key);
        return defaultValue;
    }

    @Nullable
    protected Parcelable[] getParcelableArray(@NonNull String key, @Nullable Parcelable[] defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    protected <T extends Parcelable> ArrayList<T> getParcelableArrayList(@NonNull String key, @Nullable ArrayList<T> defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArrayList(key);
        return defaultValue;
    }

    @Nullable
    protected <T extends Parcelable> SparseArray<T> getSparseParcelableArray(@NonNull String key, @Nullable SparseArray<T> defaultValue) {
        if (source.containsKey(key))
            return source.getSparseParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    protected String getString(@NonNull String key, @Nullable String defaultValue) {
        return source.getString(key, defaultValue);
    }

    @Nullable
    protected String[] getStringArray(@NonNull String key, @Nullable String[] defaultValue) {
        if (source.containsKey(key))
            return source.getStringArray(key);
        return defaultValue;
    }

    @Nullable
    protected ArrayList<String> getStringArrayList(@NonNull String key, @Nullable ArrayList<String> defaultValue) {
        if (source.containsKey(key))
            return source.getStringArrayList(key);
        return defaultValue;
    }
    //endregion

    //region Others: Parceler and Serializable.
    @Nullable
    protected Serializable getSerializable(@NonNull String key, @Nullable Serializable defaultValue) {
        if (source.containsKey(key))
            return source.getSerializable(key);
        return defaultValue;
    }
    //endregion

    protected void checkRequired(@NonNull String key, @NonNull String targetName) {
        if (!source.containsKey(key))
            throw new IllegalStateException(String.format("Required extra '%s' with key '%s' not found, if this extra is optional add @Nullable to this field.",
                    targetName, key));
    }

    @Nullable
    protected Object nextArg() {
        Object arg = args.get(0);
        args.remove(0);
        return arg;
    }
}