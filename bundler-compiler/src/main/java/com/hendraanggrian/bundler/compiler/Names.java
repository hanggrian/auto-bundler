package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.auto.common.MoreElements;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class Names {

    // internal
    static final ClassName CLASS_BUNDLE_BINDING;
    static final ClassName CLASS_BUNDLER_UTILS;
    // Android
    static final ClassName CLASS_BUNDLE;
    static final ClassName CLASS_PARCELABLE;
    static final ClassName CLASS_SPARSE_ARRAY;
    // Parceler
    static final ClassName CLASS_PARCEL;
    static final ClassName CLASS_PARCELS;

    static final String TARGET = "target";
    static final String SOURCE = "source";
    static final String ARGS = "args";

    static {
        final String bundler = "com.hendraanggrian.bundler";
        CLASS_BUNDLE_BINDING = ClassName.get(bundler, "BundleBinding");
        CLASS_BUNDLER_UTILS = ClassName.get(bundler, "BundlerUtils");

        final String android = "android";
        final String androidOs = android + ".os";
        final String androidUtil = android + ".util";
        CLASS_BUNDLE = ClassName.get(androidOs, "Bundle");
        CLASS_PARCELABLE = ClassName.get(androidOs, "Parcelable");
        CLASS_SPARSE_ARRAY = ClassName.get(androidUtil, "SparseArray");

        final String parceler = "org.parceler";
        CLASS_PARCEL = ClassName.get(parceler, "Parcel");
        CLASS_PARCELS = ClassName.get(parceler, "Parcels");
    }

    @NonNull
    static String guessGeneratedName(@NonNull TypeElement typeElement, @NonNull String suffix) {
        List<String> enclosings = Lists.newArrayList(typeElement.getSimpleName().toString());
        while (typeElement.getNestingKind().isNested()) {
            typeElement = MoreElements.asType(typeElement.getEnclosingElement());
            enclosings.add(typeElement.getSimpleName().toString());
        }
        Collections.reverse(enclosings);
        String typeName = enclosings.get(0);
        for (int i = 1; i < enclosings.size(); i++)
            typeName += '$' + enclosings.get(i);
        return typeName + suffix;
    }

    @NonNull
    static TypeName safeUnbox(@NonNull TypeName input) {
        if (input.isBoxedPrimitive())
            return input.unbox();
        return input;
    }
}