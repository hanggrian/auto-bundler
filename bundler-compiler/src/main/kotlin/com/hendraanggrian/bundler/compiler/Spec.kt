package com.hendraanggrian.bundler.compiler

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

internal interface Spec {

    val packageName: String
    val typeSpec: TypeSpec.Builder
    val methodSpecs: Iterable<MethodSpec.Builder>

    fun toJavaFile(): JavaFile {
        val typeSpecBuilder = typeSpec
        for (builder in methodSpecs) typeSpecBuilder.addMethod(builder.build())
        return JavaFile.builder(packageName, typeSpecBuilder.build())
                .addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                .build()
    }
}