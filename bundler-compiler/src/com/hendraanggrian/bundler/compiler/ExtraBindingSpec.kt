package com.hendraanggrian.bundler.compiler

import androidx.annotation.Nullable
import com.google.auto.common.MoreElements
import com.google.auto.common.MoreTypes
import com.hendraanggrian.bundler.Extra
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Types

internal class ExtraBindingSpec(typeElement: TypeElement) : BindingSpec(typeElement, Extra.SUFFIX) {

    private val constructorBinding = MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(ClassName.get(typeElement), TARGET)
        .addParameter(TYPE_BUNDLE, SOURCE)
    private val constructorWrapping = MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(List::class.java, ARGS)

    override fun superclass(extraClassNames: Collection<String>): ExtraBindingSpec {
        var extraHasSuperclass = false
        if (targetSuperclass.kind != TypeKind.NONE && targetSuperclass.kind != TypeKind.VOID) {
            val superclass = MoreTypes.asTypeElement(targetSuperclass)
            val extraClassName = superclass.getMeasuredName(suffix)
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName))
                extraHasSuperclass = true
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(TYPE_BUNDLE_BINDING)
            constructorBinding.addStatement("super(\$L)", SOURCE)
        } else {
            constructorBinding.addStatement("super(\$L, \$L)", TARGET, SOURCE)
        }
        constructorWrapping.addStatement("super(\$L)", ARGS)
        return this
    }

    override fun statement(fieldElements: Iterable<Element>, typeUtils: Types): ExtraBindingSpec {
        for (fieldElement in fieldElements) {
            val field = FieldBinding(fieldElement, typeUtils)
            if (!MoreElements.isAnnotationPresent(fieldElement, Nullable::class.java)) {
                constructorBinding.addCode(field.checkRequiredCodeBlock)
            }
            constructorBinding.addCode(field.codeBlock)
            constructorWrapping.addCode(field.putCodeBlockWithList)
        }
        return this
    }

    override val methodSpecs: Iterable<MethodSpec.Builder>
        get() = listOf(constructorBinding, constructorWrapping)
}