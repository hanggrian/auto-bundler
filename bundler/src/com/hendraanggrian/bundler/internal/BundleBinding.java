package com.hendraanggrian.bundler.internal;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.RequiresApi;

public abstract class BundleBinding {

    public static final BundleBinding EMPTY = new BundleBinding(Bundle.EMPTY) {
    };

    protected Bundle source;
    private List<?> args;

    protected BundleBinding(Bundle source) {
        this(source, Collections.emptyList());
    }

    protected BundleBinding(List<?> args) {
        this(new Bundle(), args);
    }

    private BundleBinding(Bundle source, List<?> args) {
        this.source = source;
        this.args = args;
    }

    public Bundle getSource() {
        return source;
    }

    // region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    protected boolean getBoolean(String key, boolean defaultValue) {
        return source.getBoolean(key, defaultValue);
    }

    protected Boolean getBoolean(final String key, Boolean defaultValue) {
        return find(key, defaultValue, new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return source.getBoolean(key);
            }
        });
    }

    protected boolean[] getBooleanArray(final String key, boolean[] defaultValue) {
        return find(key, defaultValue, new Callable<boolean[]>() {
            @Override
            public boolean[] call() {
                return source.getBooleanArray(key);
            }
        });
    }

    protected byte getByte(String key, byte defaultValue) {
        return source.getByte(key, defaultValue);
    }

    protected Byte getByte(final String key, Byte defaultValue) {
        return find(key, defaultValue, new Callable<Byte>() {
            @Override
            public Byte call() {
                return source.getByte(key);
            }
        });
    }

    protected byte[] getByteArray(final String key, byte[] defaultValue) {
        return find(key, defaultValue, new Callable<byte[]>() {
            @Override
            public byte[] call() {
                return source.getByteArray(key);
            }
        });
    }

    protected char getChar(String key, char defaultValue) {
        return source.getChar(key, defaultValue);
    }

    protected Character getChar(final String key, Character defaultValue) {
        return find(key, defaultValue, new Callable<Character>() {
            @Override
            public Character call() {
                return source.getChar(key);
            }
        });
    }

    protected char[] getCharArray(final String key, char[] defaultValue) {
        return find(key, defaultValue, new Callable<char[]>() {
            @Override
            public char[] call() {
                return source.getCharArray(key);
            }
        });
    }

    protected double getDouble(String key, double defaultValue) {
        return source.getDouble(key, defaultValue);
    }

    protected Double getDouble(final String key, Double defaultValue) {
        return find(key, defaultValue, new Callable<Double>() {
            @Override
            public Double call() {
                return source.getDouble(key);
            }
        });
    }

    protected double[] getDoubleArray(final String key, double[] defaultValue) {
        return find(key, defaultValue, new Callable<double[]>() {
            @Override
            public double[] call() {
                return source.getDoubleArray(key);
            }
        });
    }

    protected float getFloat(String key, float defaultValue) {
        return source.getFloat(key, defaultValue);
    }

    protected Float getFloat(final String key, Float defaultValue) {
        return find(key, defaultValue, new Callable<Float>() {
            @Override
            public Float call() {
                return source.getFloat(key);
            }
        });
    }

    protected float[] getFloatArray(final String key, float[] defaultValue) {
        return find(key, defaultValue, new Callable<float[]>() {
            @Override
            public float[] call() {
                return source.getFloatArray(key);
            }
        });
    }

    protected int getInt(String key, int defaultValue) {
        return source.getInt(key, defaultValue);
    }

    protected Integer getInt(final String key, Integer defaultValue) {
        return find(key, defaultValue, new Callable<Integer>() {
            @Override
            public Integer call() {
                return source.getInt(key);
            }
        });
    }

    protected int[] getIntArray(final String key, int[] defaultValue) {
        return find(key, defaultValue, new Callable<int[]>() {
            @Override
            public int[] call() {
                return source.getIntArray(key);
            }
        });
    }

    protected ArrayList<Integer> getIntegerArrayList(final String key, ArrayList<Integer> defaultValue) {
        return find(key, defaultValue, new Callable<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() {
                return source.getIntegerArrayList(key);
            }
        });
    }

    protected long getLong(String key, long defaultValue) {
        return source.getLong(key, defaultValue);
    }

    protected Long getLong(final String key, Long defaultValue) {
        return find(key, defaultValue, new Callable<Long>() {
            @Override
            public Long call() {
                return source.getLong(key);
            }
        });
    }

    protected long[] getLongArray(final String key, long[] defaultValue) {
        return find(key, defaultValue, new Callable<long[]>() {
            @Override
            public long[] call() {
                return source.getLongArray(key);
            }
        });
    }

    protected short getShort(String key, short defaultValue) {
        return source.getShort(key, defaultValue);
    }

    protected Short getShort(final String key, Short defaultValue) {
        return find(key, defaultValue, new Callable<Short>() {
            @Override
            public Short call() {
                return source.getShort(key);
            }
        });
    }

    protected short[] getShortArray(final String key, short[] defaultValue) {
        return find(key, defaultValue, new Callable<short[]>() {
            @Override
            public short[] call() {
                return source.getShortArray(key);
            }
        });
    }
    // endregion

    // region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    protected CharSequence getCharSequence(String key, CharSequence defaultValue) {
        return source.getCharSequence(key, defaultValue);
    }

    protected CharSequence[] getCharSequenceArray(final String key, CharSequence[] defaultValue) {
        return find(key, defaultValue, new Callable<CharSequence[]>() {
            @Override
            public CharSequence[] call() {
                return source.getCharSequenceArray(key);
            }
        });
    }

    protected ArrayList<CharSequence> getCharSequenceArrayList(final String key, ArrayList<CharSequence> defaultValue) {
        return find(key, defaultValue, new Callable<ArrayList<CharSequence>>() {
            @Override
            public ArrayList<CharSequence> call() {
                return source.getCharSequenceArrayList(key);
            }
        });
    }

    protected <T extends Parcelable> T getParcelable(final String key, T defaultValue) {
        return find(key, defaultValue, new Callable<T>() {
            @Override
            public T call() {
                return source.getParcelable(key);
            }
        });
    }

    protected <T extends Parcelable> T[] getParcelableArray(final String key, T[] defaultValue) {
        return find(key, defaultValue, new Callable<T[]>() {
            @Override
            @SuppressWarnings("unchecked")
            public T[] call() {
                return (T[]) source.getParcelableArray(key);
            }
        });
    }

    protected <T extends Parcelable> ArrayList<T> getParcelableArrayList(final String key, ArrayList<T> defaultValue) {
        return find(key, defaultValue, new Callable<ArrayList<T>>() {
            @Override
            public ArrayList<T> call() {
                return source.getParcelableArrayList(key);
            }
        });
    }

    protected <T extends Parcelable> SparseArray<T> getSparseParcelableArray(final String key, SparseArray<T> defaultValue) {
        return find(key, defaultValue, new Callable<SparseArray<T>>() {
            @Override
            public SparseArray<T> call() {
                return source.getSparseParcelableArray(key);
            }
        });
    }

    @RequiresApi(12)
    protected String getString(String key, String defaultValue) {
        return source.getString(key, defaultValue);
    }

    protected String[] getStringArray(final String key, String[] defaultValue) {
        return find(key, defaultValue, new Callable<String[]>() {
            @Override
            public String[] call() {
                return source.getStringArray(key);
            }
        });
    }

    protected ArrayList<String> getStringArrayList(final String key, ArrayList<String> defaultValue) {
        return find(key, defaultValue, new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() {
                return source.getStringArrayList(key);
            }
        });
    }
    // endregion

    // region Others: Parceler and Serializable.
    protected <T extends Serializable> T getSerializable(final String key, T defaultValue) {
        return find(key, defaultValue, new Callable<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T call() {
                return (T) source.getSerializable(key);
            }
        });
    }
    // endregion

    protected void checkRequired(String key, String targetName) {
        if (!source.containsKey(key)) {
            throw new IllegalStateException(String.format(
                "Required extra %s with key %s not found, " +
                    "if this extra is optional add @Nullable to this field.",
                targetName, key));
        }
    }

    protected Object nextArg() {
        final Object arg = args.get(0);
        args.remove(0);
        return arg;
    }

    private <T> T find(String key, T defaultValue, Callable<T> callable) {
        if (source.containsKey(key)) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
        return defaultValue;
    }
}