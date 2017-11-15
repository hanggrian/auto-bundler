package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreTypes
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import java.io.Serializable
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.util.Types

/** Represents Bundle-compatible values. */
internal enum class BundleValueType(val typeName: TypeName, private val methodName: String) {
    // Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    BOOLEAN(TypeName.BOOLEAN, "Boolean"),
    BOOLEAN_ARRAY(TypeName.get(BooleanArray::class.java), "BooleanArray"),
    BYTE(TypeName.BYTE, "Byte"),
    BYTE_ARRAY(TypeName.get(ByteArray::class.java), "ByteArray"),
    CHAR(TypeName.CHAR, "Char"),
    CHAR_ARRAY(TypeName.get(CharArray::class.java), "CharArray"),
    DOUBLE(TypeName.DOUBLE, "Double"),
    DOUBLE_ARRAY(TypeName.get(DoubleArray::class.java), "DoubleArray"),
    FLOAT(TypeName.FLOAT, "Float"),
    FLOAT_ARRAY(TypeName.get(FloatArray::class.java), "FloatArray"),
    INT(TypeName.INT, "Int"),
    INT_ARRAY(TypeName.get(IntArray::class.java), "IntArray"),
    INT_ARRAYLIST(ParameterizedTypeName.get(ArrayList::class.java, Integer::class.java), "IntegerArrayList"),
    LONG(TypeName.LONG, "Long"),
    LONG_ARRAY(TypeName.get(LongArray::class.java), "LongArray"),
    SHORT(TypeName.SHORT, "Short"),
    SHORT_ARRAY(TypeName.get(ShortArray::class.java), "ShortArray"),
    // Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    CHARSEQUENCE(TypeName.get(CharSequence::class.java), "CharSequence"),
    CHARSEQUENCE_ARRAY(TypeName.get(Array<CharSequence>::class.java), "CharSequenceArray"),
    CHARSEQUENCE_ARRAYLIST(ParameterizedTypeName.get(ArrayList::class.java, CharSequence::class.java), "CharSequenceArrayList"),
    PARCELABLE(TYPE_PARCELABLE, "Parcelable"),
    PARCELABLE_ARRAY(ArrayTypeName.of(TYPE_PARCELABLE), "ParcelableArray"),
    PARCELABLE_ARRAYLIST(ParameterizedTypeName.get(ClassName.get(ArrayList::class.java), TYPE_PARCELABLE), "ParcelableArrayList"),
    PARCELABLE_SPARSEARRAY(ParameterizedTypeName.get(TYPE_SPARSE_ARRAY, TYPE_PARCELABLE), "SparseParcelableArray"),
    STRING(TypeName.get(String::class.java), "String"),
    STRING_ARRAY(TypeName.get(Array<String>::class.java), "StringArray"),
    STRING_ARRAYLIST(ParameterizedTypeName.get(ArrayList::class.java, String::class.java), "StringArrayList"),
    // Others: Parceler and Serializable.
    PARCELER(TypeName.VOID, "Parcelable"),
    SERIALIZABLE(TypeName.get(Serializable::class.java), "Serializable");

    val getMethodName: String get() = "get$methodName"

    val putMethodName: String get() = "put$methodName"

    companion object {
        private val TypeName.safeUnboxed: TypeName get() = if (isBoxedPrimitive) unbox() else this

        fun valueOf(fieldElement: Element, typeUtils: Types): BundleValueType {
            // get all supertypes in case this element is subclass of Parcelable or Serializable
            // also add current element's kind in case element does not have supertypes
            val supertypes = typeUtils.directSupertypes(fieldElement.asType()).toMutableList()
            supertypes.add(0, fieldElement.asType())
            while (!supertypes.isEmpty()) {
                try {
                    val currentType = TypeName.get(supertypes[0])
                    for (type in values()) {
                        if (currentType is ArrayTypeName && type.typeName is ArrayTypeName) {
                            if (currentType.componentType.safeUnboxed == type.typeName.componentType.safeUnboxed)
                                return type
                        } else if ((currentType.isPrimitive || currentType.isBoxedPrimitive) && (type.typeName.isPrimitive || type.typeName.isBoxedPrimitive)) {
                            if (currentType.safeUnboxed == type.typeName.safeUnboxed)
                                return type
                        } else if (type.typeName == currentType) {
                            return type
                        }
                    }
                } catch (e: Exception) {
                    // some bad shit
                } finally {
                    supertypes.removeAt(0)
                }
            }
            // this element is not primitive and not subclass of Parcelable or Serializable
            // check if this class is parceled by parceler
            MoreTypes.asTypeElement(fieldElement.asType()).annotationMirrors
                    .filter { TypeName.get(it.annotationType) == TYPE_PARCEL }
                    .forEach { return PARCELER }
            // not supported, throw exception
            throw RuntimeException(String.format(
                    "Unsupported type: %s in %s. Is this a parceler type? Annotate the class with @Parcel.",
                    fieldElement.simpleName, fieldElement.enclosingElement.simpleName))
        }
    }
}