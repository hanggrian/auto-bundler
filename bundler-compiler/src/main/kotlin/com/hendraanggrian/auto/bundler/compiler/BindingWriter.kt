package com.hendraanggrian.auto.bundler.compiler

import androidx.annotation.Nullable
import com.google.auto.common.MoreElements
import com.google.auto.common.MoreTypes
import com.hendraanggrian.javapoet.PUBLIC
import com.hendraanggrian.javapoet.TypeSpecBuilder
import com.hendraanggrian.javapoet.buildJavaFile
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
) = buildBindingJavaFile(typeElement, "_ExtrasBinding") {
    var hasSupercls = false
    val targetSupercls = typeElement.superclass
    if (targetSupercls.kind != TypeKind.NONE && targetSupercls.kind != TypeKind.VOID) {
        val supercls = MoreTypes.asTypeElement(targetSupercls)
        val extraClassName = supercls.getMeasuredName("_ExtrasBinding")
        if (extraClassName in extraClassNames) {
            superclass = ClassName.get(typeElement.measuredPackageName, extraClassName)
            hasSupercls = true
        }
    }
    if (!hasSupercls) {
        superclass = CLS_BUNDLE_BINDING
    }
    methods {
        // binding
        addConstructor {
            addModifiers(PUBLIC)
            parameters {
                add(CLS_BUNDLE, REF_SOURCE)
                add(ClassName.get(typeElement), REF_TARGET)
            }
            when {
                !hasSupercls -> appendLine("super(\$L)", REF_SOURCE)
                else -> appendLine("super(\$L, \$L)", REF_TARGET, REF_SOURCE)
            }
            for (fieldElement in fieldElements) {
                val field = FieldBinding(fieldElement, typeUtils)
                if (!MoreElements.isAnnotationPresent(fieldElement, Nullable::class.java)) {
                    appendLine(field.checkRequiredCodeBlock)
                }
                appendLine(field.codeBlock)
            }
        }
        // wrapping
        addConstructor {
            addModifiers(PUBLIC)
            parameters.add(List::class, REF_ARGS)
            appendLine("super(\$L)", REF_ARGS)
            for (fieldElement in fieldElements) {
                appendLine(FieldBinding(fieldElement, typeUtils).putCodeBlockWithList)
            }
        }
    }
}.writeTo(this)

internal fun Filer.writeStateBinding(
    typeElement: TypeElement,
    extraClassNames: Collection<String>,
    fieldElements: Iterable<Element>,
    typeUtils: Types
) = buildBindingJavaFile(typeElement, "_StatesBinding") {
    var hasSupercls = false
    val targetSupercls = typeElement.superclass
    if (targetSupercls.kind != TypeKind.NONE && targetSupercls.kind != TypeKind.VOID) {
        val supercls = MoreTypes.asTypeElement(targetSupercls)
        val extraClassName = supercls.getMeasuredName("_StatesBinding")
        if (extraClassName in extraClassNames) {
            superclass = ClassName.get(typeElement.measuredPackageName, extraClassName)
            hasSupercls = true
        }
    }
    if (!hasSupercls) {
        superclass = CLS_BUNDLE_BINDING
    }
    methods {
        // binding
        addConstructor {
            addModifiers(PUBLIC)
            parameters.add(CLS_BUNDLE, REF_SOURCE)
            parameters.add(ClassName.get(typeElement), REF_TARGET)
            when {
                !hasSupercls -> appendLine("super(\$L)", REF_SOURCE)
                else -> appendLine("super(\$L, \$L)", REF_TARGET, REF_SOURCE)
            }
            for (fieldElement in fieldElements) {
                appendLine(FieldBinding(fieldElement, typeUtils).codeBlock)
            }
        }
        // saving
        addConstructor {
            addModifiers(PUBLIC)
            parameters.add(ClassName.get(typeElement), REF_TARGET)
            parameters.add(CLS_BUNDLE, REF_SOURCE)
            when {
                !hasSupercls -> appendLine("super(\$L)", REF_SOURCE)
                else -> append("super(\$L, \$L)", REF_SOURCE, REF_TARGET)
            }
            for (fieldElement in fieldElements) {
                appendLine(FieldBinding(fieldElement, typeUtils).putCodeBlock)
            }
        }
    }
}.writeTo(this)

private fun buildBindingJavaFile(
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
