package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Represents Bundle-compatible values.
 * List below are taken from Bundles and sorted alphabetically ascending, except for parceler.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
enum ExtraType {
    //region Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    BOOLEAN("Boolean", boolean.class),
    BOOLEAN_ARRAY("BooleanArray", boolean[].class),
    BYTE("Byte", byte.class),
    BYTE_ARRAY("ByteArray", byte[].class),
    CHAR("Char", char.class),
    CHAR_ARRAY("CharArray", char[].class),
    DOUBLE("Double", double.class),
    DOUBLE_ARRAY("DoubleArray", double[].class),
    FLOAT("Float", float.class),
    FLOAT_ARRAY("FloatArray", float[].class),
    INT("Int", int.class),
    INT_ARRAY("IntArray", int[].class),
    INT_ARRAYLIST("IntegerArrayList", ArrayList.class, Integer.class),
    LONG("Long", long.class),
    LONG_ARRAY("LongArray", long[].class),
    SHORT("Short", short.class),
    SHORT_ARRAY("ShortArray", short[].class),
    //endregion
    //region Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    CHARSEQUENCE("CharSequence", CharSequence.class),
    CHARSEQUENCE_ARRAY("CharSequenceArray", CharSequence[].class),
    CHARSEQUENCE_ARRAYLIST("CharSequenceArrayList", ArrayList.class, CharSequence.class),
    PARCELABLE("Parcelable", ClassName.get("android.os", "Parcelable")),
    PARCELABLE_ARRAY("ParcelableArray", ArrayTypeName.of(ClassName.get("android.os", "Parcelable"))),
    PARCELABLE_ARRAYLIST("ParcelableArrayList", ClassName.get(ArrayList.class), ClassName.get("android.os", "Parcelable")),
    PARCELABLE_SPARSEARRAY("SparseParcelableArray", ClassName.get("android.util", "SparseArray"), ClassName.get("android.os", "Parcelable")),
    STRING("String", String.class),
    STRING_ARRAY("StringArray", String[].class),
    STRING_ARRAYLIST("StringArrayList", ArrayList.class, String.class),
    //endregion
    //region Others: Parceler and Serializable.
    PARCELER("Parcelable", TypeName.VOID),
    SERIALIZABLE("Serializable", Serializable.class);
    //endregion

    @NonNull private final String methodName;
    @NonNull private final TypeName typeName;

    ExtraType(@NonNull String methodName, @NonNull ClassName cls, @NonNull TypeName... typeNames) {
        this(methodName, ParameterizedTypeName.get(cls, typeNames));
    }

    ExtraType(@NonNull String methodName, @NonNull Class<?> cls, @NonNull Type... types) {
        this(methodName, ParameterizedTypeName.get(cls, types));
    }

    ExtraType(@NonNull String methodName, @NonNull Type type) {
        this(methodName, TypeName.get(type));
    }

    ExtraType(@NonNull String methodName, @NonNull TypeName typeName) {
        this.methodName = methodName;
        this.typeName = typeName;
    }

    @NonNull
    String getMethodName() {
        return "get" + methodName;
    }

    @NonNull
    String putMethodName() {
        return "put" + methodName;
    }

    @NonNull
    public static ExtraType valueOf(@NonNull Types typeUtils, @NonNull Element fieldElement) {
        List<TypeMirror> supertypes = Lists.newArrayList(typeUtils.directSupertypes(fieldElement.asType())); // get all supertypes in case this element is subclass of Parcelable or Serializable
        supertypes.add(0, fieldElement.asType()); // also add current element's kind in case element does not have supertypes
        while (!supertypes.isEmpty()) {
            TypeName currentType = TypeName.get(supertypes.get(0));
            for (ExtraType type : values()) {
                if (currentType instanceof ArrayTypeName && type.typeName instanceof ArrayTypeName) {
                    if (unbox(((ArrayTypeName) currentType).componentType).equals(unbox(((ArrayTypeName) type.typeName).componentType)))
                        return type;
                } else if ((currentType.isPrimitive() || currentType.isBoxedPrimitive()) && (type.typeName.isPrimitive() || type.typeName.isBoxedPrimitive())) {
                    if (unbox(currentType).equals(unbox(type.typeName)))
                        return type;
                } else if (type.typeName.equals(currentType)) {
                    return type;
                }
            }
            supertypes.remove(0);
        }
        // this element is not primitive and not subclass of Parcelable or Serializable
        // let's assume this class is parceled by parceler
        return PARCELER;
    }

    @NonNull
    private static TypeName unbox(@NonNull TypeName input) {
        if (input.isBoxedPrimitive())
            return input.unbox();
        return input;
    }
}