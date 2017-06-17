package com.hendraanggrian.bundler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundler {

    private static final String TAG = "Bundler";
    private static boolean DEBUG;
    @Nullable private static Map<String, Constructor<? extends BundleBinding>> BINDINGS;

    private Bundler() {
    }

    public static void setDebug(boolean debug) {
        Bundler.DEBUG = debug;
    }

    /**
     * Bind extra fields in support Fragment with default behavior.
     */
    public static void bindExtras(@NonNull android.support.v4.app.Fragment target) {
        bindExtras(target, target.getArguments());
    }

    /**
     * Bind extra fields in Fragment with default behavior,
     * not available pre-11.
     */
    @RequiresApi(11)
    @TargetApi(11)
    public static void bindExtras(@NonNull Fragment target) {
        bindExtras(target, target.getArguments());
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
        bindExtras(target, source.getExtras());
    }

    /**
     * Bind extra fields in target from source Bundle.
     */
    public static <T> void bindExtras(@NonNull T target, @NonNull Bundle source) {
        BundleBinding binding = createBinding(
                target.getClass(),
                BindExtra.SUFFIX,
                new Class<?>[]{target.getClass(), Bundle.class},
                new Object[]{target, source}
        );
        if (DEBUG) printContent(binding.source);
    }

    /**
     * Bind state fields in target from source Bundle.
     */
    public static <T> void bindStates(@NonNull T target, @NonNull Bundle source) {
        BundleBinding binding = createBinding(
                target.getClass(),
                BindState.SUFFIX,
                new Class<?>[]{target.getClass(), Bundle.class},
                new Object[]{target, source}
        );
        if (DEBUG) printContent(binding.source);
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
        BundleBinding binding = createBinding(
                targetClass,
                BindExtra.SUFFIX,
                new Class<?>[]{List.class},
                new Object[]{args}
        );
        if (DEBUG) printContent(binding.source);
        return binding.source;
    }

    @NonNull
    public static Bundle onSaveInstanceState(@NonNull Bundle source, @NonNull Class<?> targetClass, @NonNull List<?> args) {
        BundleBinding binding = createBinding(
                targetClass,
                BindState.SUFFIX,
                new Class<?>[]{List.class},
                new Object[]{args}
        );
        source.putAll(binding.source);
        if (DEBUG) printContent(binding.source);
        return source;
    }

    @NonNull
    @SuppressWarnings("TryWithIdenticalCatches")
    private static BundleBinding createBinding(
            @NonNull Class<?> targetClass,
            @NonNull String targetClassSuffix,
            @NonNull Class<?>[] constructorParameterTypes,
            @NonNull Object[] constructorParameters
    ) {
        if (DEBUG) Log.d(TAG, "Looking up constructor for " + targetClass.getName());
        Constructor<? extends BundleBinding> constructor = findBinding(targetClass, targetClassSuffix, constructorParameterTypes);
        if (constructor == null) {
            if (DEBUG)
                Log.d(TAG, "Ignored because no constructor was found in " + targetClass.getSimpleName());
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
        String bindingKey = targetClass.toString() + constructorParameterTypes.length;
        Constructor<? extends BundleBinding> binding = BINDINGS.get(bindingKey);
        if (binding != null) {
            if (DEBUG) Log.d(TAG, "HIT: Cache found in binding weak map.");
            return binding;
        }
        String targetClassName = targetClass.getName();
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (DEBUG) Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            binding = (Constructor<? extends BundleBinding>) targetClass
                    .getClassLoader()
                    .loadClass(targetClassName + targetClassSuffix)
                    .getConstructor(constructorParameterTypes);
            if (DEBUG) Log.d(TAG, "HIT: Loaded binding class, caching in weak map.");
        } catch (ClassNotFoundException e) {
            if (DEBUG)
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            Class<?> targetSuperclass = targetClass.getSuperclass();
            //region abstract classes binding fix
            if (constructorParameterTypes[0] == targetClass && constructorParameterTypes[0].getSuperclass() == targetSuperclass) {
                constructorParameterTypes[0] = targetSuperclass;
            }
            //endregion
            binding = findBinding(targetSuperclass, targetClassSuffix, constructorParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + targetClassName, e);
        }
        BINDINGS.put(bindingKey, binding);
        return binding;
    }

    private static void printContent(@NonNull Bundle bundle) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (String key : bundle.keySet()) {
                jsonObject.put(key, bundle.get(key));
            }
            String json = jsonObject.toString(4);
            for (String line : json.split("\\r?\\n")) {
                Log.d(TAG, line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}