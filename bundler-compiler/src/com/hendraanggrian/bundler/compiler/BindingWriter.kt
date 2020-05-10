package com.hendraanggrian.bundler.compiler

import androidx.annotation.Nullable
import com.google.auto.common.MoreElements
import com.google.auto.common.MoreTypes
import com.hendraanggrian.bundler.BindExtra
import com.hendraanggrian.bundler.BindState
import com.hendraanggrian.javapoet.MethodSpecBuilder
import com.hendraanggrian.javapoet.TypeSpecBuilder
import com.hendraanggrian.javapoet.buildJavaFile
import com.hendraanggrian.javapoet.collections.MethodSpecListScope
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Types

internal fun Filer.writeExtraBinding(
    typeElement: TypeElement,
    extraClassNames: Collection<String>,
    fieldElements: Iterable<Element>,
    typeUtils: Types
) = buildBindingJavaFile(typeElement, BindExtra.SUFFIX) {
    var hasSupercls = false
    val targetSupercls = typeElement.superclass
    if (targetSupercls.kind != TypeKind.NONE && targetSupercls.kind != TypeKind.VOID) {
        val supercls = MoreTypes.asTypeElement(targetSupercls)
        val extraClassName = supercls.getMeasuredName(BindExtra.SUFFIX)
        if (extraClassName in extraClassNames) {
            superclass = ClassName.get(typeElement.measuredPackageName, extraClassName)
            hasSupercls = true
        }
    }
    if (!hasSupercls) {
        superclass = BUNDLE_BINDING
    }
    methods {
        // binding
        constructor {
            parameters.add(ClassName.get(typeElement), TARGET)
            parameters.add(BUNDLE, SOURCE)
            when {
                !hasSupercls -> appendln("super(\$L)", SOURCE)
                else -> appendln("super(\$L, \$L)", TARGET, SOURCE)
            }
            for (fieldElement in fieldElements) {
                val field = FieldBinding(fieldElement, typeUtils)
                if (!MoreElements.isAnnotationPresent(fieldElement, Nullable::class.java)) {
                    appendln(field.checkRequiredCodeBlock)
                }
                appendln(field.codeBlock)
            }
        }
        // wrapping
        constructor {
            parameters.add(List::class, ARGS)
            appendln("super(\$L)", ARGS)
            for (fieldElement in fieldElements) {
                appendln(FieldBinding(fieldElement, typeUtils).putCodeBlockWithList)
            }
        }
    }
}.writeTo(this)

internal fun Filer.writeStateBinding(
    typeElement: TypeElement,
    extraClassNames: Collection<String>,
    fieldElements: Iterable<Element>,
    typeUtils: Types
) = buildBindingJavaFile(typeElement, BindState.SUFFIX) {
    var hasSupercls = false
    val targetSupercls = typeElement.superclass
    if (targetSupercls.kind != TypeKind.NONE && targetSupercls.kind != TypeKind.VOID) {
        val supercls = MoreTypes.asTypeElement(targetSupercls)
        val extraClassName = supercls.getMeasuredName(BindState.SUFFIX)
        if (extraClassName in extraClassNames) {
            superclass = ClassName.get(typeElement.measuredPackageName, extraClassName)
            hasSupercls = true
        }
    }
    if (!hasSupercls) {
        superclass = BUNDLE_BINDING
    }
    methods {
        // binding
        constructor {
            parameters.add(ClassName.get(typeElement), TARGET)
            parameters.add(BUNDLE, SOURCE)
            when {
                !hasSupercls -> appendln("super(\$L)", SOURCE)
                else -> appendln("super(\$L, \$L)", TARGET, SOURCE)
            }
            for (fieldElement in fieldElements) {
                appendln(FieldBinding(fieldElement, typeUtils).codeBlock)
            }
        }
        // saving
        constructor {
            parameters.add(BUNDLE, SOURCE)
            parameters.add(ClassName.get(typeElement), TARGET)
            when {
                !hasSupercls -> appendln("super(\$L)", SOURCE)
                else -> append("super(\$L, \$L)", SOURCE, TARGET)
            }
            for (fieldElement in fieldElements) {
                appendln(FieldBinding(fieldElement, typeUtils).putCodeBlock)
            }
        }
    }
}.writeTo(this)

private inline fun MethodSpecListScope.constructor(builderAction: MethodSpecBuilder.() -> Unit) = addConstructor {
    addModifiers(Modifier.PUBLIC)
    builderAction()
}

private inline fun buildBindingJavaFile(
    typeElement: TypeElement,
    suffix: String,
    builderAction: TypeSpecBuilder.() -> Unit
): JavaFile = buildJavaFile(typeElement.measuredPackageName) {
    comment = FILE_COMMENT
    addClass(typeElement.getMeasuredName(suffix)) {
        addModifiers(Modifier.PUBLIC)
        builderAction()
    }
}
