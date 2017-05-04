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
    BOOLEAN("getBoolean", boolean.class),
    BOOLEAN_ARRAY("getBooleanArray", boolean[].class),
    BYTE("getByte", byte.class),
    // BYTE_BOXED("getByte", Byte.class),
    BYTE_ARRAY("getByteArray", byte[].class),
    CHAR("getChar", char.class),
    CHAR_ARRAY("getCharArray", char[].class),
    CHARSEQUENCE("getCharSequence", CharSequence.class),
    CHARSEQUENCE_ARRAY("getCharSequenceArray", CharSequence[].class),
    CHARSEQUENCE_ARRAYLIST("getCharSequenceArrayList", ArrayList.class, CharSequence.class),
    DOUBLE("getDouble", double.class),
    DOUBLE_ARRAY("getDoubleArray", double[].class),
    FLOAT("getFloat", float.class),
    FLOAT_ARRAY("getFloatArray", float[].class),
    LONG("getLong", long.class),
    LONG_ARRAY("getLongArray", long[].class),
    INT("getInt", int.class),
    INT_ARRAY("getIntArray", int[].class),
    INT_ARRAYLIST("getIntegerArrayList", ArrayList.class, Integer.class),
    PARCELABLE("getParcelable", ClassName.get("android.os", "Parcelable")),
    PARCELABLE_ARRAY("getParcelableArray", ArrayTypeName.of(ClassName.get("android.os", "Parcelable"))),
    PARCELABLE_ARRAYLIST("getParcelableArrayList", ClassName.get(ArrayList.class), ClassName.get("android.os", "Parcelable")),
    PARCELABLE_SPARSEARRAY("getSparseParcelableArray", ClassName.get("android.util", "SparseArray"), ClassName.get("android.os", "Parcelable")),
    SERIALIZABLE("getSerializable", Serializable.class),
    SHORT("getShort", short.class),
    SHORT_ARRAY("getShortArray", short[].class),
    STRING("getString", String.class),
    STRING_ARRAY("getStringArray", String[].class),
    STRING_ARRAYLIST("getStringArrayList", ArrayList.class, String.class),
    PARCELER("getParceler", TypeName.VOID);

    @NonNull final String methodName;
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
    public static ExtraType valueOf(@NonNull Types typeUtils, @NonNull Element fieldElement) {
        List<TypeMirror> supertypes = Lists.newArrayList(typeUtils.directSupertypes(fieldElement.asType())); // get all supertypes in case this element is subclass of Parcelable or Serializable
        supertypes.add(0, fieldElement.asType()); // also add current element's kind in case element does not have supertypes
        while (!supertypes.isEmpty()) {
            TypeName currentType = TypeName.get(supertypes.get(0));
            for (ExtraType type : values()) {
                if ((type.typeName.isPrimitive() || type.typeName.isBoxedPrimitive()) && type.typeName.unbox().equals(currentType))
                    return type;
                else if (type.typeName.equals(currentType))
                    return type;
            }
            supertypes.remove(0);
        }
        // this element is not primitive and not subclass of Parcelable or Serializable
        // let's assume this class is parceled by parceler
        return PARCELER;
    }
}