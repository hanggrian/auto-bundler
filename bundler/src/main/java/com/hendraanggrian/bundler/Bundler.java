package com.hendraanggrian.bundler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundler {

    static final String TAG = Bundler.class.getSimpleName();
    static boolean debug;

    private static Map<Class<?>, Constructor<? extends ExtraBinding>> bindings;

    private Bundler() {
    }

    public static void setDebug(boolean debug) {
        Bundler.debug = debug;
    }

    public static void bind(@NonNull Activity target) {
        Intent intent = target.getIntent();
        if (intent != null)
            bind(target, intent);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Activity.");
    }

    public static <T extends Context> void bind(@NonNull T target, @NonNull Intent source) {
        Bundle bundle = source.getExtras();
        if (bundle != null)
            bind(target, bundle);
        else if (debug)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Intent.");
    }

    public static <T extends Context> void bind(@NonNull T target, @NonNull Bundle source) {
        bind(target, source, target.getResources());
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static <T> void bind(@NonNull T target, @NonNull Bundle source, @NonNull Resources res) {
        final Class<?> targetClass = target.getClass();
        if (debug)
            Log.d(TAG, "Looking up binding for " + targetClass.getName());
        Constructor<? extends ExtraBinding> constructor = findBinding(targetClass);
        if (constructor == null) {
            if (debug)
                Log.d(TAG, "Ignored because no binding was found in " + targetClass.getSimpleName());
            return;
        }
        try {
            constructor.newInstance(target, source, res);
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
    @SuppressWarnings("unchecked")
    private static Constructor<? extends ExtraBinding> findBinding(@NonNull Class<?> targetClass) {
        if (bindings == null)
            bindings = new WeakHashMap<>();
        Constructor<? extends ExtraBinding> binding = bindings.get(targetClass);
        if (binding != null) {
            if (debug)
                Log.d(TAG, "HIT: Cache found in binding weak map.");
            return binding;
        }
        String targetClassName = targetClass.getName();
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (debug)
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            Class<?> bindingClass = targetClass.getClassLoader().loadClass(targetClassName + "_ExtraBinding");
            binding = (Constructor<? extends ExtraBinding>) bindingClass.getConstructor(targetClass, Bundle.class, Resources.class);
            if (debug)
                Log.d(TAG, "HIT: Loaded binding class, caching in weak map.");
        } catch (ClassNotFoundException e) {
            if (debug)
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            binding = findBinding(targetClass.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + targetClassName, e);
        }
        bindings.put(targetClass, binding);
        return binding;
    }
}