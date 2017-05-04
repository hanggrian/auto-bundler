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
 * Some getters of Bundle do not support default value,
 * this utility class make sure that all getters can have default value in generated code.
 * Functions below are sorted according to Bundle and BaseBundle of API 25 Platform.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundles {

    //region Parceler
    @Nullable
    public static <T> T getParceler(@NonNull Bundle source, @NonNull String key, @Nullable T defaultValue) {
        return source.containsKey(key)
                ? Parcels.<T>unwrap(source.getParcelable(key))
                : defaultValue;
    }
    //endregion

    //region Bundle
    public static byte getByte(@NonNull Bundle source, @NonNull String key, byte defaultValue) {
        return source.containsKey(key)
                ? source.getByte(key, defaultValue)
                : defaultValue;
    }

    public static char getChar(@NonNull Bundle source, @NonNull String key, char defaultValue) {
        return source.getChar(key, defaultValue);
    }

    public static short getShort(@NonNull Bundle source, @NonNull String key, short defaultValue) {
        return source.getShort(key, defaultValue);
    }

    public static float getFloat(@NonNull Bundle source, @NonNull String key, float defaultValue) {
        return source.getFloat(key, defaultValue);
    }

    @Nullable
    public static CharSequence getCharSequence(@NonNull Bundle source, @NonNull String key, @Nullable CharSequence defaultValue) {
        return source.getCharSequence(key, defaultValue);
    }

    @Nullable
    public static <T extends Parcelable> T getParcelable(@NonNull Bundle source, @NonNull String key, @Nullable T defaultValue) {
        return source.containsKey(key)
                ? source.<T>getParcelable(key)
                : defaultValue;
    }

    @Nullable
    public static Parcelable[] getParcelableArray(@NonNull Bundle source, @NonNull String key, @Nullable Parcelable[] defaultValue) {
        return source.containsKey(key)
                ? source.getParcelableArray(key)
                : defaultValue;
    }

    @Nullable
    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<T> defaultValue) {
        return source.containsKey(key)
                ? source.<T>getParcelableArrayList(key)
                : defaultValue;
    }

    @Nullable
    public static <T extends Parcelable> SparseArray<T> getSparseParcelableArray(@NonNull Bundle source, @NonNull String key, @Nullable SparseArray<T> defaultValue) {
        return source.containsKey(key)
                ? source.<T>getSparseParcelableArray(key)
                : defaultValue;
    }

    @Nullable
    public static Serializable getSerializable(@NonNull Bundle source, @NonNull String key, @Nullable Serializable defaultValue) {
        return source.containsKey(key)
                ? source.getSerializable(key)
                : defaultValue;
    }

    @Nullable
    public static ArrayList<Integer> getIntegerArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<Integer> defaultValue) {
        return source.containsKey(key)
                ? source.getIntegerArrayList(key)
                : defaultValue;
    }

    @Nullable
    public static ArrayList<String> getStringArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<String> defaultValue) {
        return source.containsKey(key)
                ? source.getStringArrayList(key)
                : defaultValue;
    }

    @Nullable
    public static ArrayList<CharSequence> getCharSequenceArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<CharSequence> defaultValue) {
        return source.containsKey(key)
                ? source.getCharSequenceArrayList(key)
                : defaultValue;
    }

    @Nullable
    public static byte[] getByteArray(@NonNull Bundle source, @NonNull String key, @Nullable byte[] defaultValue) {
        return source.containsKey(key)
                ? source.getByteArray(key)
                : defaultValue;
    }

    @Nullable
    public static short[] getShortArray(@NonNull Bundle source, @NonNull String key, @Nullable short[] defaultValue) {
        return source.containsKey(key)
                ? source.getShortArray(key)
                : defaultValue;
    }

    @Nullable
    public static char[] getCharArray(@NonNull Bundle source, @NonNull String key, @Nullable char[] defaultValue) {
        return source.containsKey(key)
                ? source.getCharArray(key)
                : defaultValue;
    }

    @Nullable
    public static float[] getFloatArray(@NonNull Bundle source, @NonNull String key, @Nullable float[] defaultValue) {
        return source.containsKey(key)
                ? source.getFloatArray(key)
                : defaultValue;
    }

    @Nullable
    public static CharSequence[] getCharSequenceArray(@NonNull Bundle source, @NonNull String key, @Nullable CharSequence[] defaultValue) {
        return source.containsKey(key)
                ? source.getCharSequenceArray(key)
                : defaultValue;
    }
    //endregion

    //region BaseBundle
    public static boolean getBoolean(@NonNull Bundle source, @NonNull String key, boolean defaultValue) {
        return source.getBoolean(key, defaultValue);
    }

    public static int getInt(@NonNull Bundle source, @NonNull String key, int defaultValue) {
        return source.getInt(key, defaultValue);
    }

    public static long getLong(@NonNull Bundle source, @NonNull String key, long defaultValue) {
        return source.getLong(key, defaultValue);
    }

    public static double getDouble(@NonNull Bundle source, @NonNull String key, double defaultValue) {
        return source.getDouble(key, defaultValue);
    }

    @Nullable
    public static String getString(@NonNull Bundle source, @NonNull String key, @Nullable String defaultValue) {
        return source.getString(key, defaultValue);
    }

    @Nullable
    public static boolean[] getBooleanArray(@NonNull Bundle source, @NonNull String key, @Nullable boolean[] defaultValue) {
        return source.containsKey(key)
                ? source.getBooleanArray(key)
                : defaultValue;
    }

    @Nullable
    public static int[] getIntArray(@NonNull Bundle source, @NonNull String key, @Nullable int[] defaultValue) {
        return source.containsKey(key)
                ? source.getIntArray(key)
                : defaultValue;
    }

    @Nullable
    public static long[] getLongArray(@NonNull Bundle source, @NonNull String key, @Nullable long[] defaultValue) {
        return source.containsKey(key)
                ? source.getLongArray(key)
                : defaultValue;
    }

    @Nullable
    public static double[] getDoubleArray(@NonNull Bundle source, @NonNull String key, @Nullable double[] defaultValue) {
        return source.containsKey(key)
                ? source.getDoubleArray(key)
                : defaultValue;
    }

    @Nullable
    public static String[] getStringArray(@NonNull Bundle source, @NonNull String key, @Nullable String[] defaultValue) {
        return source.containsKey(key)
                ? source.getStringArray(key)
                : defaultValue;
    }
    //endregion

    public static void checkRequired(@NonNull Bundle source, @NonNull String key, @NonNull String targetName) {
        if (!source.containsKey(key))
            throw new IllegalStateException(String.format("Required extra '%s' with key '%s' not found, if this extra is optional add @Nullable to this field.", targetName, key));
    }
}