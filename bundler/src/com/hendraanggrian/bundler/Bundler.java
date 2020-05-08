package com.hendraanggrian.bundler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hendraanggrian.bundler.internal.BundleBinding;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Bundler {
    private static final String TAG = "Bundler";
    private static Map<String, Constructor<? extends BundleBinding>> BINDINGS;
    private static boolean DEBUG = false;

    private Bundler() {
    }

    /**
     * When set to true, will print Bundler operation in debug level.
     */
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * Bind extra fields in Fragment with default behavior.
     */
    public static void bindExtras(@NonNull Fragment target) {
        final Bundle args = target.getArguments();
        if (args == null) {
            throw new IllegalStateException();
        }
        bindExtras(target, args);
    }

    /**
     * Bind extra fields in Fragment with default behavior.
     */
    public static void bindExtras(@NonNull androidx.fragment.app.Fragment target) {
        final Bundle args = target.getArguments();
        if (args == null) {
            throw new IllegalStateException();
        }
        bindExtras(target, args);
    }

    /**
     * Bind extra fields in Activity with default behavior.
     */
    public static void bindExtras(@NonNull Activity target) {
        bindExtras(target, target.getIntent());
    }

    /**
     * Bind extra fields in target from source Bundle attached to Intent.
     */
    public static <T> void bindExtras(@NonNull T target, @NonNull Intent source) {
        final Bundle extras = source.getExtras();
        if (extras == null) {
            throw new IllegalStateException();
        }
        bindExtras(target, extras);
    }

    /**
     * Bind extra fields in target from source Bundle.
     */
    public static <T> void bindExtras(@NonNull T target, @NonNull Bundle source) {
        final BundleBinding binding = createBinding(
            target.getClass(),
            Extra.SUFFIX,
            new Class<?>[]{target.getClass(), Bundle.class},
            new Object[]{target, source}
        );
        if (DEBUG) {
            print(binding.getSource());
        }
    }

    @NonNull
    public static Bundle wrapExtras(@NonNull Class<?> targetClass, @NonNull Object arg) {
        return wrapExtras(targetClass, new ArrayList<>(Collections.singletonList(arg)));
    }

    @NonNull
    public static Bundle wrapExtras(@NonNull Class<?> targetClass, @NonNull Object... args) {
        return wrapExtras(targetClass, new ArrayList<>(Arrays.asList(args)));
    }

    @NonNull
    public static Bundle wrapExtras(@NonNull Class<?> targetClass, @NonNull List<?> args) {
        final BundleBinding binding = createBinding(
            targetClass,
            Extra.SUFFIX,
            new Class<?>[]{List.class},
            new Object[]{args}
        );
        final Bundle extras = binding.getSource();
        if (DEBUG) {
            print(extras);
        }
        return extras;
    }

    /**
     * Bind state fields in target from source Bundle.
     */
    public static <T> void restoreStates(@NonNull T target, @NonNull Bundle source) {
        final BundleBinding binding = createBinding(
            target.getClass(),
            State.SUFFIX,
            new Class<?>[]{target.getClass(), Bundle.class},
            new Object[]{target, source}
        );
        if (DEBUG) {
            print(binding.getSource());
        }
    }

    @NonNull
    public static <T> Bundle saveStates(@NonNull T target, @NonNull Bundle source) {
        final BundleBinding binding = createBinding(
            target.getClass(),
            State.SUFFIX,
            new Class<?>[]{Bundle.class, target.getClass()},
            new Object[]{source, target}
        );
        final Bundle states = binding.getSource();
        source.putAll(states);
        if (DEBUG) {
            print(states);
        }
        return source;
    }

    @NonNull
    private static BundleBinding createBinding(
        @NonNull Class<?> targetClass,
        @NonNull String targetClassSuffix,
        @NonNull Class<?>[] constructorParameterTypes,
        @NonNull Object[] constructorParameters
    ) {
        if (DEBUG) {
            Log.d(TAG, "Looking up constructor for " + targetClass.getName());
        }
        final Constructor<? extends BundleBinding> constructor =
            findBinding(targetClass, targetClassSuffix, constructorParameterTypes);
        if (constructor == null) {
            if (DEBUG) {
                Log.d(
                    TAG,
                    "Ignored because no constructor was found in " + targetClass.getSimpleName()
                );
            }
            return BundleBinding.EMPTY;
        }
        try {
            return constructor.newInstance(constructorParameters);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException)
                throw (RuntimeException) cause;
            if (cause instanceof Error)
                throw (Error) cause;
            throw new RuntimeException("Unable to create constructor instance.", cause);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static Constructor<? extends BundleBinding> findBinding(
        @NonNull Class<?> targetClass,
        @NonNull String targetClassSuffix,
        @NonNull Class<?>[] constructorParameterTypes
    ) {
        if (BINDINGS == null) {
            BINDINGS = new WeakHashMap<>();
        }
        final String bindingKey = targetClass.getCanonicalName() +
            targetClassSuffix +
            constructorParameterTypes[0].getSimpleName();
        Constructor<? extends BundleBinding> binding = BINDINGS.get(bindingKey);
        if (binding != null) {
            if (DEBUG) {
                Log.d(TAG, "HIT: Cache found in binding weak map.");
            }
            return binding;
        }
        final String targetClassName = targetClass.getName();
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (DEBUG) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return null;
        }
        try {
            binding = (Constructor<? extends BundleBinding>) targetClass
                .getClassLoader()
                .loadClass(targetClassName + targetClassSuffix)
                .getConstructor(constructorParameterTypes);
            if (DEBUG) {
                Log.d(TAG, "HIT: Loaded binding class, caching in weak map.");
            }
        } catch (ClassNotFoundException e) {
            if (DEBUG) {
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            }
            final Class<?> targetSuperclass = targetClass.getSuperclass();
            //region abstract classes binding fix
            if (constructorParameterTypes[0] == targetClass &&
                constructorParameterTypes[0].getSuperclass() == targetSuperclass) {
                constructorParameterTypes[0] = targetSuperclass;
            }
            //endregion
            binding = findBinding(targetSuperclass, targetClassSuffix, constructorParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                "Unable to find binding constructor for " + targetClassName, e
            );
        }
        BINDINGS.put(bindingKey, binding);
        return binding;
    }

    private static void print(Bundle bundle) {
        try {
            final JSONObject jsonObject = new JSONObject();
            for (final String key : bundle.keySet()) {
                jsonObject.put(key, bundle.get(key));
            }
            final String json = jsonObject.toString(4);
            final List<String> split = new ArrayList<>(Arrays.asList(json.split("\\r?\\n")));
            final int lastIndex = split.size() - 1;
            if (split.get(lastIndex).isEmpty()) {
                split.remove(lastIndex);
            }
            for (final String line : split) {
                Log.d(TAG, line);
            }
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }
}