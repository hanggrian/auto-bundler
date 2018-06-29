package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreTypes
import com.hendraanggrian.bundler.State
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import java.util.Arrays
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Types

internal class StateBindingSpec(typeElement: TypeElement) : BindingSpec(typeElement, State.SUFFIX) {

    private val constructorBinding = MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(ClassName.get(typeElement), TARGET)
        .addParameter(TYPE_BUNDLE, SOURCE)
    private val constructorSaving = MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(TYPE_BUNDLE, SOURCE)
        .addParameter(ClassName.get(typeElement), TARGET)

    override fun superclass(extraClassNames: Collection<String>): StateBindingSpec {
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
            constructorSaving.addStatement("super(\$L)", SOURCE)
        } else {
            constructorBinding.addStatement("super(\$L, \$L)", TARGET, SOURCE)
            constructorSaving.addStatement("super(\$L, \$L)", SOURCE, TARGET)
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