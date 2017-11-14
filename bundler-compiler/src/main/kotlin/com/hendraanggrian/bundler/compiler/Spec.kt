package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreElements
import com.google.common.collect.Lists
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ClassName.get
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.lang.model.element.TypeElement

internal abstract class Spec {

    internal abstract val packageName: String
    internal abstract val typeSpec: TypeSpec.Builder
    internal abstract val methodSpecs: Iterable<MethodSpec.Builder>

    fun toJavaFile(): JavaFile {
        val typeSpecBuilder = typeSpec
        for (builder in methodSpecs) typeSpecBuilder.addMethod(builder.build())
        return JavaFile.builder(packageName, typeSpecBuilder.build())
                .addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                .build()
    }

    companion object {

        // internal
        val CLASS_BUNDLE_BINDING = get("com.hendraanggrian.bundler.internal", "BundleBinding")!!
        val CLASS_BUNDLER_UTILS = get("com.hendraanggrian.bundler", "BundlerUtils")!!
        // Android
        val CLASS_BUNDLE: ClassName
        val CLASS_PARCELABLE: ClassName
        val CLASS_SPARSE_ARRAY: ClassName
        // Parceler
        val CLASS_PARCEL: ClassName
        val CLASS_PARCELS: ClassName

        val TARGET = "target"
        val SOURCE = "source"
        val ARGS = "args"

        init {
            val android = "android"
            val androidOs = android + ".os"
            val androidUtil = android + ".util"
            CLASS_BUNDLE = get(androidOs, "Bundle")
            CLASS_PARCELABLE = get(androidOs, "Parcelable")
            CLASS_SPARSE_ARRAY = get(androidUtil, "SparseArray")

            val parceler = "org.parceler"
            CLASS_PARCEL = get(parceler, "Parcel")
            CLASS_PARCELS = get(parceler, "Parcels")
        }

        fun guessGeneratedName(typeElement: TypeElement, suffix: String): String {
            var _typeElement = typeElement
            val enclosings = Lists.newArrayList(_typeElement.simpleName.toString())
            while (_typeElement.nestingKind.isNested) {
                _typeElement = MoreElements.asType(_typeElement.enclosingElement)
                enclosings.add(_typeElement.simpleName.toString())
            }
            Collections.reverse(enclosings)
            var typeName = enclosings[0]
            for (i in 1 until enclosings.size)
                typeName += '$' + enclosings[i]
            return typeName + suffix
        }
    }
}