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
 * Consistent API for all Bundle and BaseBundle getters.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundles {

    //region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    public static boolean getBoolean(@NonNull Bundle source, @NonNull String key, boolean defaultValue) {
        return source.getBoolean(key, defaultValue);
    }

    @Nullable
    public static Boolean getBoolean(@NonNull Bundle source, @NonNull String key, @Nullable Boolean defaultValue) {
        if (source.containsKey(key))
            return source.getBoolean(key);
        return defaultValue;
    }

    @Nullable
    public static boolean[] getBooleanArray(@NonNull Bundle source, @NonNull String key, @Nullable boolean[] defaultValue) {
        if (source.containsKey(key))
            return source.getBooleanArray(key);
        return defaultValue;
    }

    public static byte getByte(@NonNull Bundle source, @NonNull String key, byte defaultValue) {
        return source.getByte(key, defaultValue);
    }

    @Nullable
    public static Byte getByte(@NonNull Bundle source, @NonNull String key, @Nullable Byte defaultValue) {
        if (source.containsKey(key))
            return source.getByte(key);
        return defaultValue;
    }

    @Nullable
    public static byte[] getByteArray(@NonNull Bundle source, @NonNull String key, @Nullable byte[] defaultValue) {
        if (source.containsKey(key))
            return source.getByteArray(key);
        return defaultValue;
    }

    public static char getChar(@NonNull Bundle source, @NonNull String key, char defaultValue) {
        return source.getChar(key, defaultValue);
    }

    @Nullable
    public static Character getChar(@NonNull Bundle source, @NonNull String key, @Nullable Character defaultValue) {
        if (source.containsKey(key))
            return source.getChar(key);
        return defaultValue;
    }

    @Nullable
    public static char[] getCharArray(@NonNull Bundle source, @NonNull String key, @Nullable char[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharArray(key);
        return defaultValue;
    }

    public static double getDouble(@NonNull Bundle source, @NonNull String key, double defaultValue) {
        return source.getDouble(key, defaultValue);
    }

    @Nullable
    public static Double getDouble(@NonNull Bundle source, @NonNull String key, @Nullable Double defaultValue) {
        if (source.containsKey(key))
            return source.getDouble(key);
        return defaultValue;
    }

    @Nullable
    public static double[] getDoubleArray(@NonNull Bundle source, @NonNull String key, @Nullable double[] defaultValue) {
        if (source.containsKey(key))
            return source.getDoubleArray(key);
        return defaultValue;
    }

    public static float getFloat(@NonNull Bundle source, @NonNull String key, float defaultValue) {
        return source.getFloat(key, defaultValue);
    }

    @Nullable
    public static Float getFloat(@NonNull Bundle source, @NonNull String key, @Nullable Float defaultValue) {
        if (source.containsKey(key))
            return source.getFloat(key);
        return defaultValue;
    }

    @Nullable
    public static float[] getFloatArray(@NonNull Bundle source, @NonNull String key, @Nullable float[] defaultValue) {
        if (source.containsKey(key))
            return source.getFloatArray(key);
        return defaultValue;
    }

    public static int getInt(@NonNull Bundle source, @NonNull String key, int defaultValue) {
        return source.getInt(key, defaultValue);
    }

    @Nullable
    public static Integer getInt(@NonNull Bundle source, @NonNull String key, @Nullable Integer defaultValue) {
        if (source.containsKey(key))
            return source.getInt(key);
        return defaultValue;
    }

    @Nullable
    public static int[] getIntArray(@NonNull Bundle source, @NonNull String key, @Nullable int[] defaultValue) {
        if (source.containsKey(key))
            return source.getIntArray(key);
        return defaultValue;
    }

    @Nullable
    public static ArrayList<Integer> getIntegerArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<Integer> defaultValue) {
        if (source.containsKey(key))
            return source.getIntegerArrayList(key);
        return defaultValue;
    }

    public static long getLong(@NonNull Bundle source, @NonNull String key, long defaultValue) {
        return source.getLong(key, defaultValue);
    }

    @Nullable
    public static Long getLong(@NonNull Bundle source, @NonNull String key, @Nullable Long defaultValue) {
        if (source.containsKey(key))
            return source.getLong(key);
        return defaultValue;
    }

    @Nullable
    public static long[] getLongArray(@NonNull Bundle source, @NonNull String key, @Nullable long[] defaultValue) {
        if (source.containsKey(key))
            return source.getLongArray(key);
        return defaultValue;
    }

    public static short getShort(@NonNull Bundle source, @NonNull String key, short defaultValue) {
        return source.getShort(key, defaultValue);
    }

    @Nullable
    public static Short getShort(@NonNull Bundle source, @NonNull String key, @Nullable Short defaultValue) {
        if (source.containsKey(key))
            return source.getShort(key);
        return defaultValue;
    }

    @Nullable
    public static short[] getShortArray(@NonNull Bundle source, @NonNull String key, @Nullable short[] defaultValue) {
        if (source.containsKey(key))
            return source.getShortArray(key);
        return defaultValue;
    }
    //endregion

    //region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    @Nullable
    public static CharSequence getCharSequence(@NonNull Bundle source, @NonNull String key, @Nullable CharSequence defaultValue) {
        return source.getCharSequence(key, defaultValue);
    }

    @Nullable
    public static CharSequence[] getCharSequenceArray(@NonNull Bundle source, @NonNull String key, @Nullable CharSequence[] defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArray(key);
        return defaultValue;
    }

    @Nullable
    public static ArrayList<CharSequence> getCharSequenceArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<CharSequence> defaultValue) {
        if (source.containsKey(key))
            return source.getCharSequenceArrayList(key);
        return defaultValue;
    }

    @Nullable
    public static <T extends Parcelable> T getParcelable(@NonNull Bundle source, @NonNull String key, @Nullable T defaultValue) {
        if (source.containsKey(key))
            return source.getParcelable(key);
        return defaultValue;
    }

    @Nullable
    public static Parcelable[] getParcelableArray(@NonNull Bundle source, @NonNull String key, @Nullable Parcelable[] defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<T> defaultValue) {
        if (source.containsKey(key))
            return source.getParcelableArrayList(key);
        return defaultValue;
    }

    @Nullable
    public static <T extends Parcelable> SparseArray<T> getSparseParcelableArray(@NonNull Bundle source, @NonNull String key, @Nullable SparseArray<T> defaultValue) {
        if (source.containsKey(key))
            return source.getSparseParcelableArray(key);
        return defaultValue;
    }

    @Nullable
    public static String getString(@NonNull Bundle source, @NonNull String key, @Nullable String defaultValue) {
        return source.getString(key, defaultValue);
    }

    @Nullable
    public static String[] getStringArray(@NonNull Bundle source, @NonNull String key, @Nullable String[] defaultValue) {
        if (source.containsKey(key))
            return source.getStringArray(key);
        return defaultValue;
    }

    @Nullable
    public static ArrayList<String> getStringArrayList(@NonNull Bundle source, @NonNull String key, @Nullable ArrayList<String> defaultValue) {
        if (source.containsKey(key))
            return source.getStringArrayList(key);
        return defaultValue;
    }
    //endregion

    //region Others: Parceler and Serializable.
    @Nullable
    public static <T> T getParceler(@NonNull Bundle source, @NonNull String key, @Nullable T defaultValue) {
        if (source.containsKey(key))
            return Parcels.unwrap(source.getParcelable(key));
        return defaultValue;
    }

    @Nullable
    public static Serializable getSerializable(@NonNull Bundle source, @NonNull String key, @Nullable Serializable defaultValue) {
        if (source.containsKey(key))
            source.getSerializable(key);
        return defaultValue;
    }
    //endregion

    public static void checkRequired(@NonNull Bundle source, @NonNull String key, @NonNull String targetName) {
        if (!source.containsKey(key))
            throw new IllegalStateException(String.format("Required extra '%s' with key '%s' not found, if this extra is optional add @Nullable to this field.", targetName, key));
    }
}