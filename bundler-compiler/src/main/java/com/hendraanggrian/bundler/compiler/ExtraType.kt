package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreTypes
import com.google.common.collect.Lists
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import java.io.Serializable
import java.lang.reflect.Type
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.util.Types

/**
 * Represents Bundle-compatible values.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
internal enum class ExtraType(private val methodName: String, val typeName: TypeName) {
    // Non-void primitive types: supports unboxed, boxed, and unboxed array (only int supports ArrayList).
    BOOLEAN("Boolean", Boolean::class.java),
    BOOLEAN_ARRAY("BooleanArray", BooleanArray::class.java),
    BYTE("Byte", Byte::class.java),
    BYTE_ARRAY("ByteArray", ByteArray::class.java),
    CHAR("Char", Char::class.java),
    CHAR_ARRAY("CharArray", CharArray::class.java),
    DOUBLE("Double", Double::class.java),
    DOUBLE_ARRAY("DoubleArray", DoubleArray::class.java),
    FLOAT("Float", Float::class.java),
    FLOAT_ARRAY("FloatArray", FloatArray::class.java),
    INT("Int", Int::class.java),
    INT_ARRAY("IntArray", IntArray::class.java),
    INT_ARRAYLIST("IntegerArrayList", ArrayList::class.java, Int::class.java),
    LONG("Long", Long::class.java),
    LONG_ARRAY("LongArray", LongArray::class.java),
    SHORT("Short", Short::class.java),
    SHORT_ARRAY("ShortArray", ShortArray::class.java),
    // Non-primitive types: supports single object, array, and ArrayList (only Parcelable supports SparseArray).
    CHARSEQUENCE("CharSequence", CharSequence::class.java),
    CHARSEQUENCE_ARRAY("CharSequenceArray", Array<CharSequence>::class.java),
    CHARSEQUENCE_ARRAYLIST("CharSequenceArrayList", ArrayList::class.java, CharSequence::class.java),
    PARCELABLE("Parcelable", Spec.CLASS_PARCELABLE),
    PARCELABLE_ARRAY("ParcelableArray", ArrayTypeName.of(Spec.CLASS_PARCELABLE)),
    PARCELABLE_ARRAYLIST("ParcelableArrayList", ClassName.get(ArrayList::class.java), Spec.CLASS_PARCELABLE),
    PARCELABLE_SPARSEARRAY("SparseParcelableArray", Spec.CLASS_SPARSE_ARRAY, Spec.CLASS_PARCELABLE),
    STRING("String", String::class.java),
    STRING_ARRAY("StringArray", Array<String>::class.java),
    STRING_ARRAYLIST("StringArrayList", ArrayList::class.java, String::class.java),
    // Others: Parceler and Serializable.
    PARCELER("Parcelable", TypeName.VOID),
    SERIALIZABLE("Serializable", Serializable::class.java);

    private constructor(methodName: String, cls: ClassName, vararg typeNames: TypeName) : this(methodName, ParameterizedTypeName.get(cls, *typeNames)) {}

    private constructor(methodName: String, cls: Class<*>, vararg types: Type) : this(methodName, ParameterizedTypeName.get(cls, *types)) {}

    private constructor(methodName: String, type: Type) : this(methodName, TypeName.get(type)) {}

    val getMethodName: String get() = "get" + methodName

    val putMethodName: String get() = "put" + methodName

    companion object {

        private val TypeName.safeUnboxed: TypeName get() = if (isBoxedPrimitive) unbox() else this

        fun valueOf(fieldElement: Element, typeUtils: Types): ExtraType {
            // get all supertypes in case this element is subclass of Parcelable or Serializable
            // also add current element's kind in case element does not have supertypes
            val supertypes = Lists.newArrayList(typeUtils.directSupertypes(fieldElement.asType()))
            supertypes.add(0, fieldElement.asType())
            while (!supertypes.isEmpty()) {
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
                supertypes.removeAt(0)
            }
            // this element is not primitive and not subclass of Parcelable or Serializable
            // check if this class is parceled by parceler
            MoreTypes.asTypeElement(fieldElement.asType()).annotationMirrors
                    .filter { TypeName.get(it.annotationType) == Spec.CLASS_PARCEL }
                    .forEach { return PARCELER }
            // not supported, throw exception
            throw RuntimeException(String.format(
                    "Unsupported type: %s in %s. Is this a parceler type? Annotate the class with @Parcel.",
                    fieldElement.simpleName, fieldElement.enclosingElement.simpleName))
        }
    }
}