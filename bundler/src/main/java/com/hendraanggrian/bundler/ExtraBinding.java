package com.hendraanggrian.bundler;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class ExtraBinding {

    @NonNull protected final Bundle source;
    @NonNull protected final Resources res;

    protected ExtraBinding(@NonNull Bundle source, @NonNull Resources res) {
        this.source = source;
        this.res = res;
    }

    protected byte getValue(@NonNull String key, byte defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        byte value = source.getByte(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + value);
        return value;
    }

    @Nullable
    protected Byte getValue(@NonNull String key, Byte defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        Byte value = source.getByte(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    protected char getValue(@NonNull String key, char defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        char value = source.getChar(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + value);
        return value;
    }

    protected short getValue(@NonNull String key, short defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        short value = source.getShort(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + value);
        return value;
    }

    protected float getValue(@NonNull String key, float defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        float value = source.getFloat(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + value);
        return value;
    }

    @Nullable
    protected CharSequence getValue(@NonNull String key, CharSequence defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        CharSequence value = source.getCharSequence(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected Size getValue(@NonNull String key, Size defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        final Size value;
        if (source.containsKey(key))
            value = source.getSize(key);
        else
            value = defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected SizeF getValue(@NonNull String key, SizeF defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        SizeF value = source.containsKey(key)
                ? source.getSizeF(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    @Nullable
    protected <T extends Parcelable> T getValue(@NonNull String key, @Nullable T defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        T value = source.containsKey(key)
                ? source.<T>getParcelable(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    @Nullable
    protected Parcelable[] getValue(@NonNull String key, @Nullable Parcelable[] defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        Parcelable[] value = source.containsKey(key)
                ? source.<Parcelable[]>getParcelableArray(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : Arrays.toString(value)));
        return value;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends Parcelable> ArrayList<T> getValue(@NonNull String key, @Nullable ArrayList<T> defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        ArrayList<T> value = source.containsKey(key)
                ? (ArrayList<T>) source.getParcelableArrayList(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : Arrays.toString(value.toArray())));
        return value;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends Parcelable> SparseArray<T> getValue(@NonNull String key, @Nullable SparseArray<T> defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        SparseArray<T> value = source.containsKey(key)
                ? (SparseArray<T>) source.getSparseParcelableArray(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    @Nullable
    protected Serializable getValue(@NonNull String key, @Nullable Serializable defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        Serializable value = source.containsKey(key)
                ? source.getSerializable(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    /*@Nullable
    protected ArrayList<Integer> getValue(@NonNull String key, @Nullable ArrayList<Integer> defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        ArrayList<Integer> value = source.containsKey(key)
                ? source.getIntegerArrayList(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : Arrays.toString(value.toArray())));
        return value;
    }

    @Nullable
    protected <T> ArrayList<T> getValue(@NonNull String key, @Nullable ArrayList<T> defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        final ArrayList<T> value;
        if (!source.containsKey(key))
            value = defaultValue;
        else if (defaultValue instanceof ArrayList<>)
        ArrayList<Integer> value = source.containsKey(key)
                ? source.getIntegerArrayList(key)
                : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : Arrays.toString(value.toArray())));
        return value;
    }*/


    @Nullable
    protected String getValue(@NonNull String key, @Nullable String defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        String value = source.getString(key, defaultValue);
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value));
        return value;
    }

    // Parceler
    @Nullable
    protected <T> T getValue(@NonNull String key, @Nullable T defaultValue, boolean required, @NonNull String fieldName) {
        checkRequired(source, key, required, fieldName);
        Parcelable parcel = source.getParcelable(key);
        T value = parcel != null ? Parcels.<T>unwrap(parcel) : defaultValue;
        if (Bundler.debug)
            Log.d(Bundler.TAG, fieldName + " <- " + (value == null ? "null" : value.toString()));
        return value;
    }

    private static void checkRequired(@NonNull Bundle source, @NonNull String key, boolean required, @NonNull String fieldName) {
        if (required && !source.containsKey(key))
            throw new IllegalStateException(String.format("Required extra '%s' with key '%s' not found, if this extra is optional add @Nullable to this field.", fieldName, key));
    }
}