package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreTypes
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Types

internal class StateBindingSpec(typeElement: TypeElement) : BindingSpec(typeElement, "_StateBinding") {

    private val constructorBinding = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get(typeElement), Spec.Companion.TARGET)
            .addParameter(Spec.Companion.CLASS_BUNDLE, Spec.Companion.SOURCE)
    private val constructorSaving = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Spec.Companion.CLASS_BUNDLE, Spec.Companion.SOURCE)
            .addParameter(ClassName.get(typeElement), Spec.Companion.TARGET)

    override fun superclass(extraClassNames: Collection<String>): StateBindingSpec {
        var extraHasSuperclass = false
        if (targetSuperclass.kind != TypeKind.NONE && targetSuperclass.kind != TypeKind.VOID) {
            val superclass = MoreTypes.asTypeElement(targetSuperclass)
            val extraClassName = Spec.Companion.guessGeneratedName(superclass, suffix)
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName))
                extraHasSuperclass = true
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(Spec.Companion.CLASS_BUNDLE_BINDING)
            constructorBinding.addStatement("super(\$L)", Spec.Companion.SOURCE)
            constructorSaving.addStatement("super(\$L)", Spec.Companion.SOURCE)
        } else {
            constructorBinding.addStatement("super(\$L, \$L)", Spec.Companion.TARGET, Spec.Companion.SOURCE)
            constructorSaving.addStatement("super(\$L, \$L)", Spec.Companion.SOURCE, Spec.Companion.TARGET)
        }
        return this
    }

    override fun statement(fieldElements: Iterable<Element>, typeUtils: Types): StateBindingSpec {
        for (fieldElement in fieldElements) {
            val field = FieldBinding(fieldElement, typeUtils)
            constructorBinding.addCode(field.codeBlock)
            constructorSaving.addCode(field.putCodeBlock)
        }
        return this
    }

    override val methodSpecs: Iterable<MethodSpec.Builder>
        get() = Arrays.asList(constructorBinding, constructorSaving)
}