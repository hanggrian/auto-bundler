package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreElements
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Types

internal abstract class BindingSpec(typeElement: TypeElement, val suffix: String) : Spec {

    override val packageName = MoreElements.getPackage(typeElement).qualifiedName.toString()
    val targetSuperclass = typeElement.superclass!!
    override val typeSpec = TypeSpec.classBuilder(typeElement.getMeasuredName(suffix))
            .addModifiers(Modifier.PUBLIC)!!

    internal abstract fun superclass(extraClassNames: Collection<String>): BindingSpec
    internal abstract fun statement(fieldElements: Iterable<Element>, typeUtils: Types): BindingSpec
}