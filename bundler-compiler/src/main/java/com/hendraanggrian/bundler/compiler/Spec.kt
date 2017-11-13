package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreElements
import com.google.common.collect.Lists
import com.squareup.javapoet.*
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
        val CLASS_BUNDLE_BINDING: ClassName
        val CLASS_BUNDLER_UTILS: ClassName
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
            val bundler = "com.hendraanggrian.bundler"
            CLASS_BUNDLE_BINDING = ClassName.get(bundler, "BundleBinding")
            CLASS_BUNDLER_UTILS = ClassName.get(bundler, "BundlerUtils")

            val android = "android"
            val androidOs = android + ".os"
            val androidUtil = android + ".util"
            CLASS_BUNDLE = ClassName.get(androidOs, "Bundle")
            CLASS_PARCELABLE = ClassName.get(androidOs, "Parcelable")
            CLASS_SPARSE_ARRAY = ClassName.get(androidUtil, "SparseArray")

            val parceler = "org.parceler"
            CLASS_PARCEL = ClassName.get(parceler, "Parcel")
            CLASS_PARCELS = ClassName.get(parceler, "Parcels")
        }

        fun guessGeneratedName(typeElement: TypeElement, suffix: String): String {
            var typeElement = typeElement
            val enclosings = Lists.newArrayList(typeElement.simpleName.toString())
            while (typeElement.nestingKind.isNested) {
                typeElement = MoreElements.asType(typeElement.enclosingElement)
                enclosings.add(typeElement.simpleName.toString())
            }
            Collections.reverse(enclosings)
            var typeName = enclosings[0]
            for (i in 1 until enclosings.size)
                typeName += '$' + enclosings[i]
            return typeName + suffix
        }
    }
}