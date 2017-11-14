package com.hendraanggrian.bundler.compiler

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.squareup.javapoet.*
import java.util.*
import javax.lang.model.element.Modifier.*

internal class UtilsSpec : Spec {

    override val typeSpec: TypeSpec.Builder
    private val methodSpecGet: MethodSpec.Builder
    private val methodSpecPut: MethodSpec.Builder

    init {
        val generic = TypeVariableName.get(GENERIC)
        val parameterSpecs = Arrays.asList<ParameterSpec>(
                ParameterSpec.builder(Bundle::class.java, SOURCE)
                        .addAnnotation(NonNull::class.java)
                        .build(),
                ParameterSpec.builder(String::class.java, KEY)
                        .addAnnotation(NonNull::class.java)
                        .build()
        )
        this.typeSpec = TypeSpec.classBuilder(TYPE_BUNDLER_UTILS.simpleName())
                .addModifiers(PUBLIC, FINAL)
        this.methodSpecGet = MethodSpec.methodBuilder("getParceler")
                .addTypeVariable(generic)
                .addModifiers(PUBLIC, STATIC, FINAL)
                .addParameters(parameterSpecs)
                .addParameter(ParameterSpec.builder(generic, DEFAULT_VALUE)
                        .addAnnotation(Nullable::class.java)
                        .build())
                .addCode(CodeBlock.of("if ($SOURCE.containsKey($KEY))\n" +
                        "return \$$GENERIC.unwrap($SOURCE.getParcelable($KEY));\n", TYPE_PARCELS))
                .addStatement("return $DEFAULT_VALUE")
                .returns(generic)
        this.methodSpecPut = MethodSpec.methodBuilder("putParceler")
                .addTypeVariable(generic)
                .addModifiers(PUBLIC, STATIC, FINAL)
                .addParameters(parameterSpecs)
                .addParameter(ParameterSpec.builder(generic, VALUE)
                        .addAnnotation(NonNull::class.java)
                        .build())
                .addStatement("$SOURCE.putParcelable($KEY, \$$GENERIC.wrap($VALUE))", TYPE_PARCELS)
    }

    override val packageName: String get() = "com.hendraanggrian.bundler"

    override val methodSpecs: Iterable<MethodSpec.Builder> get() = listOf(methodSpecGet, methodSpecPut)
}