package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.auto.common.MoreTypes;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import static com.hendraanggrian.bundler.compiler.Names.safeUnbox;

/**
 * Represents Bundle-compatible values.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
enum ExtraType {
    // Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
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
    // Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    CHARSEQUENCE("CharSequence", CharSequence.class),
    CHARSEQUENCE_ARRAY("CharSequenceArray", CharSequence[].class),
    CHARSEQUENCE_ARRAYLIST("CharSequenceArrayList", ArrayList.class, CharSequence.class),
    PARCELABLE("Parcelable", Names.CLASS_PARCELABLE),
    PARCELABLE_ARRAY("ParcelableArray", ArrayTypeName.of(Names.CLASS_PARCELABLE)),
    PARCELABLE_ARRAYLIST("ParcelableArrayList", ClassName.get(ArrayList.class), Names.CLASS_PARCELABLE),
    PARCELABLE_SPARSEARRAY("SparseParcelableArray", Names.CLASS_SPARSE_ARRAY, Names.CLASS_PARCELABLE),
    STRING("String", String.class),
    STRING_ARRAY("StringArray", String[].class),
    STRING_ARRAYLIST("StringArrayList", ArrayList.class, String.class),
    // Others: Parceler and Serializable.
    PARCELER("Parcelable", TypeName.VOID),
    SERIALIZABLE("Serializable", Serializable.class);

    @NonNull private final String methodName;
    @NonNull final TypeName typeName;

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
    public static ExtraType valueOf(@NonNull Element fieldElement, @NonNull Types typeUtils) {
        // get all supertypes in case this element is subclass of Parcelable or Serializable
        // also add current element's kind in case element does not have supertypes
        List<TypeMirror> supertypes = Lists.newArrayList(typeUtils.directSupertypes(fieldElement.asType()));
        supertypes.add(0, fieldElement.asType());
        while (!supertypes.isEmpty()) {
            TypeName currentType = TypeName.get(supertypes.get(0));
            for (ExtraType type : values()) {
                if (currentType instanceof ArrayTypeName && type.typeName instanceof ArrayTypeName) {
                    if (safeUnbox(((ArrayTypeName) currentType).componentType).equals(safeUnbox(((ArrayTypeName) type.typeName).componentType)))
                        return type;
                } else if ((currentType.isPrimitive() || currentType.isBoxedPrimitive()) && (type.typeName.isPrimitive() || type.typeName.isBoxedPrimitive())) {
                    if (safeUnbox(currentType).equals(safeUnbox(type.typeName)))
                        return type;
                } else if (type.typeName.equals(currentType)) {
                    return type;
                }
            }
            supertypes.remove(0);
        }
        // this element is not primitive and not subclass of Parcelable or Serializable
        // check if this class is parceled by parceler
        for (AnnotationMirror annotationMirror : MoreTypes.asTypeElement(fieldElement.asType()).getAnnotationMirrors())
            if (TypeName.get(annotationMirror.getAnnotationType()).equals(Names.CLASS_PARCEL))
                return PARCELER;
        // not supported, throw exception
        throw new RuntimeException(String.format(
                "Unsupported type: %s in %s. Is this a parceler type? Annotate the class with @Parcel.",
                fieldElement.getSimpleName(), fieldElement.getEnclosingElement().getSimpleName()));
    }
}