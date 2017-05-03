package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
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
    PARCELER(new Object(), "getParceler"),
    BOOLEAN(boolean.class, "getBoolean"),
    BOOLEAN_ARRAY(ArrayTypeName.of(boolean.class), "getBooleanArray"),
    BYTE(byte.class, "getByte"),
    BYTE_BOXED(Byte.class, "getByte"),
    BYTE_ARRAY(ArrayTypeName.of(byte.class), "getByteArray"),
    CHAR(char.class, "getChar"),
    CHAR_ARRAY(ArrayTypeName.of(char.class), "getCharArray"),
    CHARSEQUENCE(ClassName.get(CharSequence.class), "getCharSequence"),
    CHARSEQUENCE_ARRAY(ArrayTypeName.of(CharSequence.class), "getCharSequenceArray"),
    CHARSEQUENCE_ARRAYLIST(ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get(CharSequence.class)), "getCharSequenceArrayList"),
    DOUBLE(double.class, "getDouble"),
    DOUBLE_ARRAY(ArrayTypeName.of(double.class), "getDoubleArray"),
    FLOAT(float.class, "getFloat"),
    FLOAT_ARRAY(ArrayTypeName.of(float.class), "getFloatArray"),
    LONG(long.class, "getLong"),
    LONG_ARRAY(ArrayTypeName.of(long.class), "getLongArray"),
    INT(int.class, "getInt"),
    INT_ARRAY(ArrayTypeName.of(int.class), "getIntArray"),
    INT_ARRAYLIST(ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get(Integer.class)), "getIntArrayList"),
    PARCELABLE(ClassName.get("android.os", "Parcelable"), "getParcelable"),
    PARCELABLE_ARRAY(ArrayTypeName.of(ClassName.get("android.os", "Parcelable")), "getParcelableArray"),
    PARCELABLE_ARRAYLIST(ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get("android.os", "Parcelable")), "getParcelableArrayList"),
    PARCELABLE_SPARSEARRAY(ParameterizedTypeName.get(ClassName.get("android.util", "SparseArray"), ClassName.get("android.os", "Parcelable")), "getSparseParcelableArray"),
    SERIALIZABLE(Serializable.class, "getSerializable"),
    SHORT(short.class, "getShort"),
    SHORT_ARRAY(ArrayTypeName.of(short.class), "getShortArray"),
    STRING(ClassName.get(String.class), "getString"),
    STRING_ARRAY(ArrayTypeName.of(String.class), "getStringArray"),
    STRING_ARRAYLIST(ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get(String.class)), "getStringArrayList");

    @NonNull private final Object typeName;
    @NonNull final String methodName;

    ExtraType(@NonNull Object typeName, @NonNull String methodName) {
        this.typeName = typeName;
        this.methodName = methodName;
    }

    @NonNull
    public static ExtraType valueOf(@NonNull Types typeUtils, @NonNull Element fieldElement) {
        List<TypeMirror> supertypes = Lists.newArrayList(typeUtils.directSupertypes(fieldElement.asType())); // get all supertypes in case this element is subclass of Parcelable or Serializable
        supertypes.add(0, fieldElement.asType()); // also add current element's kind in case element does not have supertypes
        while (!supertypes.isEmpty()) {
            TypeMirror currentType = supertypes.get(0);
            for (ExtraType type : values()) {
                if (type.typeName instanceof Class && ((Class) type.typeName).getSimpleName().equals(currentType.toString()))
                    return type;
                else if (type.typeName instanceof TypeName && (TypeName.get(currentType)).equals(type.typeName))
                    return type;
            }
            supertypes.remove(0);
        }
        // this element is not primitive and not subclass of Parcelable or Serializable
        // let's assume this class is parceled by parceler
        return PARCELER;
    }
}