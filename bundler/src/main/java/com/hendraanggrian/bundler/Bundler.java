package com.hendraanggrian.bundler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hendraanggrian.bundler.annotations.BindExtra;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Bundler {

    private static final String TAG = Bundler.class.getSimpleName();
    private static boolean debug;
    @Nullable private static Map<Class<?>, Constructor<? extends ExtraBinding>> bindings;

    private Bundler() {
    }

    public static void setDebug(boolean debug) {
        Bundler.debug = debug;
    }

    public static void bind(@NonNull android.support.v4.app.Fragment target) {
        bind(target, target.getArguments());
    }

    public static void bind(@NonNull Fragment target) {
        bind(target, target.getArguments());
    }

    public static void bind(@NonNull Activity target) {
        bind(target, target.getIntent());
    }

    public static <T> void bind(@NonNull T target, @Nullable Intent source) {
        if (source == null)
            Log.d(TAG, "bind() ignored because Bundle is not found from this Intent.");
        else
            bind(target, source.getExtras());
    }

    public static <T> void bind(@NonNull T target, @Nullable Bundle source) {
        if (source == null)
            Log.d(TAG, "bind() ignored because Bundle is null.");
        else
            createBinding(target.getClass(),
                    new Class<?>[]{target.getClass(), Bundle.class},
                    new Object[]{target, source}
            );
    }

    @NonNull
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull Object arg) {
        return wrap(targetClass, new ArrayList<>(Collections.singletonList(arg)));
    }

    @NonNull
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull Object... args) {
        return wrap(targetClass, new ArrayList<>(Arrays.asList(args)));
    }

    @NonNull
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull Collection<?> args) {
        return createBinding(targetClass,
                new Class<?>[]{List.class},
                new Object[]{new ShrinkingArrayList<>(args)}
        ).source;
    }

    @NonNull
    @SuppressWarnings("TryWithIdenticalCatches")
    private static <T extends ExtraBinding> ExtraBinding createBinding(
            @NonNull Class<?> targetClass,
            @NonNull Class<?>[] constructorParameterTypes,
            @NonNull Object[] constructorParameters
    ) {
        if (debug)
            Log.d(TAG, "Looking up constructor for " + targetClass.getName());
        Constructor<? extends ExtraBinding> constructor = findBinding(targetClass, constructorParameterTypes);
        if (constructor == null) {
            if (debug)
                Log.d(TAG, "Ignored because no constructor was found in " + targetClass.getSimpleName());
            return ExtraBinding.EMPTY;
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
    private static Constructor<? extends ExtraBinding> findBinding(
            @NonNull Class<?> targetClass,
            @NonNull Class<?>... constructorParameterTypes
    ) {
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
            binding = (Constructor<? extends ExtraBinding>) targetClass
                    .getClassLoader()
                    .loadClass(targetClassName + BindExtra.SUFFIX)
                    .getConstructor(constructorParameterTypes);
            if (debug)
                Log.d(TAG, "HIT: Loaded binding class, caching in weak map.");
        } catch (ClassNotFoundException e) {
            if (debug)
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            Class<?> targetSuperclass = targetClass.getSuperclass();
            //region abstract classes binding fix
            if (constructorParameterTypes[0] == targetClass && constructorParameterTypes[0].getSuperclass() == targetSuperclass)
                constructorParameterTypes[0] = targetSuperclass;
            //endregion
            binding = findBinding(targetSuperclass, constructorParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + targetClassName, e);
        }
        bindings.put(targetClass, binding);
        return binding;
    }
}