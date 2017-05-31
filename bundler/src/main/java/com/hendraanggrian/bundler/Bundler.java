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

import com.hendraanggrian.bundler.annotations.BindExtra;

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
    private static boolean debug;
    @Nullable private static Map<String, Constructor<? extends BundleBinding>> bindings;

    private Bundler() {
    }

    public static void setDebug(boolean debug) {
        Bundler.debug = debug;
    }

    public static void bind(@NonNull android.support.v4.app.Fragment target) {
        bind(target, target.getArguments());
    }

    @RequiresApi(11)
    @TargetApi(11)
    public static void bind(@NonNull Fragment target) {
        bind(target, target.getArguments());
    }

    public static void bind(@NonNull Activity target) {
        bind(target, target.getIntent());
    }

    public static <T> void bind(@NonNull T target, @NonNull Intent source) {
        bind(target, source.getExtras());
    }

    public static <T> void bind(@NonNull T target, @NonNull Bundle source) {
        BundleBinding binding = createBinding(target.getClass(),
                new Class<?>[]{target.getClass(), Bundle.class},
                new Object[]{target, source}
        );
        if (debug)
            Log.d(TAG, toString(target.getClass(), binding.source));
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
    public static Bundle wrap(@NonNull Class<?> targetClass, @NonNull List<?> args) {
        BundleBinding binding = createBinding(targetClass,
                new Class<?>[]{List.class},
                new Object[]{args}
        );
        if (debug)
            Log.d(TAG, toString(targetClass, binding.source));
        return binding.source;
    }

    @NonNull
    @SuppressWarnings("TryWithIdenticalCatches")
    private static BundleBinding createBinding(
            @NonNull Class<?> targetClass,
            @NonNull Class<?>[] constructorParameterTypes,
            @NonNull Object[] constructorParameters
    ) {
        if (debug)
            Log.d(TAG, "Looking up constructor for " + targetClass.getName());
        Constructor<? extends BundleBinding> constructor = findBinding(targetClass, constructorParameterTypes);
        if (constructor == null) {
            if (debug)
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
            @NonNull Class<?>... constructorParameterTypes
    ) {
        if (bindings == null)
            bindings = new WeakHashMap<>();
        String bindingKey = targetClass.toString() + constructorParameterTypes.length;
        Constructor<? extends BundleBinding> binding = bindings.get(bindingKey);
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
            binding = (Constructor<? extends BundleBinding>) targetClass
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
        bindings.put(bindingKey, binding);
        return binding;
    }

    @NonNull
    public static String toString(@NonNull Class<?> cls, @NonNull Bundle bundle) {
        String content = "";
        for (String key : bundle.keySet())
            content += key + "=" + bundle.get(key) + ", ";
        if (content.endsWith(", "))
            content = content.substring(0, content.length() - 2);
        return cls.getSimpleName() + "[" + content + "]";
    }
}