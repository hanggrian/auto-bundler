package com.hendraanggrian.bundler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.WrapExtras;

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
    @Nullable private static Map<String, Constructor<?>> cachingMap;

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
        if (source == null) {
            Log.d(TAG, "bind() ignored because Bundle is not found from this Intent.");
            return;
        }
        bind(target, source.getExtras());
    }

    public static <T> void bind(@NonNull T target, @Nullable Bundle source) {
        if (source == null) {
            Log.d(TAG, "bind() ignored because Bundle is null.");
            return;
        }
        newConstructorInstance(
                target.getClass(),
                BindExtra.SUFFIX,
                new Class<?>[]{target.getClass(), Bundle.class},
                new Object[]{target, source},
                ExtraBinding.EMPTY
        );
    }

    @NonNull
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull Object... args) {
        return wrap(targetClass, new ArrayList<>(Arrays.asList(args)));
    }

    @NonNull
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull List<?> args) {
        return Bundler.newConstructorInstance(
                targetClass,
                WrapExtras.SUFFIX,
                new Class<?>[]{List.class},
                new Object[]{args},
                ExtrasWrapping.EMPTY
        ).source;
    }

    @NonNull
    @SuppressWarnings("TryWithIdenticalCatches")
    private static <T> T newConstructorInstance(
            @NonNull Class<?> targetClass,
            @NonNull String clsNameSuffix,
            @NonNull Class<?>[] constructorArgClasses,
            @NonNull Object[] constructorArgs,
            @NonNull T defaultValue) {
        if (debug)
            Log.d(TAG, "Looking up constructor for " + targetClass.getName());
        Constructor<T> constructor = findConstructor(targetClass, clsNameSuffix, constructorArgClasses);
        if (constructor == null) {
            if (debug)
                Log.d(TAG, "Ignored because no constructor was found in " + targetClass.getSimpleName());
            return defaultValue;
        }
        try {
            return constructor.newInstance(constructorArgs);
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
    private static <T> Constructor<T> findConstructor(
            @NonNull Class<?> targetClass,
            @NonNull String clsNameSuffix,
            @NonNull Class<?>[] constructorArgClasses) {
        if (cachingMap == null)
            cachingMap = new WeakHashMap<>();
        String targetClassName = targetClass.getName();
        String generatedClassName = targetClassName + clsNameSuffix;
        Constructor<T> constructor = (Constructor<T>) cachingMap.get(generatedClassName);
        if (constructor != null) {
            if (debug)
                Log.d(TAG, "HIT: Cache found in constructor weak map.");
            return constructor;
        }
        if (targetClassName.startsWith("android.") || targetClassName.startsWith("java.")) {
            if (debug)
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            constructor = (Constructor<T>) targetClass
                    .getClassLoader()
                    .loadClass(generatedClassName)
                    .getConstructor(constructorArgClasses);
            if (debug)
                Log.d(TAG, "HIT: Loaded constructor class, caching in weak map.");
        } catch (ClassNotFoundException e) {
            if (debug)
                Log.d(TAG, "Not found. Trying superclass " + targetClass.getSuperclass().getName());
            Class<?> targetSuperclass = targetClass.getSuperclass();
            //region abstract classes binding fix
            if (constructorArgClasses[0] == targetClass && constructorArgClasses[0].getSuperclass() == targetSuperclass)
                constructorArgClasses[0] = targetSuperclass;
            //endregion
            constructor = findConstructor(targetSuperclass, clsNameSuffix, constructorArgClasses);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor constructor for " + targetClassName, e);
        }
        cachingMap.put(generatedClassName, constructor);
        return constructor;
    }
}