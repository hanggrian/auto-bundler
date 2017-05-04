package com.hendraanggrian.bundler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundler {

    private static final String TAG = Bundler.class.getSimpleName();
    private static boolean debug;
    @Nullable private static Map<String, Constructor<? extends Binding>> bindings;

    private Bundler() {
    }

    public static void setDebug(boolean debug) {
        Bundler.debug = debug;
    }

    public static void bind(@NonNull android.support.v4.app.Fragment target) {
        Bundle bundle = target.getArguments();
        if (bundle != null)
            bind(target, bundle);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Fragment.");
    }

    public static void bind(@NonNull Fragment target) {
        Bundle bundle = target.getArguments();
        if (bundle != null)
            bind(target, bundle);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Fragment.");
    }

    public static void bind(@NonNull Activity target) {
        Intent intent = target.getIntent();
        if (intent != null)
            bind(target, intent);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Activity.");
    }

    public static <T> void bind(@NonNull T target, @NonNull Intent source) {
        Bundle bundle = source.getExtras();
        if (bundle != null)
            bind(target, bundle);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Intent.");
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static <T> void bind(@NonNull T target, @NonNull Bundle source) {
        final Class<?> targetClass = target.getClass();
        if (debug)
            Log.d(TAG, "Looking up binding for " + targetClass.getName());
        Constructor<? extends ExtraBinding> constructor = findExtraBinding(targetClass);
        if (constructor == null) {
            if (debug)
                Log.d(TAG, "Ignored because no binding was found in " + targetClass.getSimpleName());
            return;
        }
        try {
            constructor.newInstance(target, source);
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
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    @NonNull
    public static Bundle wrap(Class<?> targetClass, Object... args) {
        return wrap(targetClass, new ArrayList<>(Arrays.asList(args)));
    }

    @NonNull
    @SuppressWarnings("TryWithIdenticalCatches")
    public static Bundle wrap(Class<?> targetClass, List args) {
        if (debug)
            Log.d(TAG, "Looking up binding for " + targetClass.getName());
        Constructor<? extends ExtrasBinding> constructor = findExtrasBinding(targetClass);
        if (constructor == null) {
            if (debug)
                Log.d(TAG, "Ignored because no binding was found in " + targetClass.getSimpleName());
            return new Bundle();
        }
        try {
            return constructor.newInstance(args).source;
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
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    @Nullable
    private static Constructor<? extends ExtraBinding> findExtraBinding(@NonNull Class<?> targetClass) {
        return findBinding(targetClass, "_ExtraBinding", targetClass, Bundle.class);
    }

    @Nullable
    private static Constructor<? extends ExtrasBinding> findExtrasBinding(@NonNull Class<?> targetClass) {
        return findBinding(targetClass, "_ExtrasBinding", List.class);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends Binding> Constructor<T> findBinding(@NonNull Class<?> targetClass, @NonNull String clsNameSuffix, @NonNull Class<?>... constructorArgs) {
        if (bindings == null)
            bindings = new WeakHashMap<>();
        final String targetClassName = targetClass.getName();
        final String generatedClassName = targetClassName + clsNameSuffix;
        Constructor<T> binding = (Constructor<T>) bindings.get(generatedClassName);
        if (binding != null) {
            if (debug)
                Log.d(TAG, "HIT: Cache found in binding weak map.");
            return binding;
        }
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (debug)
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            binding = (Constructor<T>) targetClass
                    .getClassLoader()
                    .loadClass(generatedClassName)
                    .getConstructor(constructorArgs);
            if (debug)
                Log.d(TAG, "HIT: Loaded binding class, caching in weak map.");
        } catch (ClassNotFoundException e) {
            if (debug)
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            binding = findBinding(targetClass.getSuperclass(), clsNameSuffix, constructorArgs);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + targetClassName, e);
        }
        bindings.put(generatedClassName, binding);
        return binding;
    }
}