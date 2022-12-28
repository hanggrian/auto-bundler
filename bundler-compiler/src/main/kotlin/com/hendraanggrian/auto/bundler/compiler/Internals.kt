@file:Suppress("UnstableApiUsage")

package com.hendraanggrian.auto.bundler.compiler

import com.google.auto.common.MoreElements
import com.hendraanggrian.javapoet.classNameOf
import javax.lang.model.element.TypeElement

internal const val PACKAGE_NAME = "com.hendraanggrian.auto.bundler"
internal const val FILE_COMMENT = "Bundler generated class, do not modify! " +
    "https://github.com/hendraanggrian/auto-bundler/"

internal const val REF_TARGET = "target"
internal const val REF_SOURCE = "source"
internal const val REF_ARGS = "args"
internal const val REF_KEY = "key"
internal const val REF_VALUE = "value"
internal const val REF_DEFAULT_VALUE = "defaultValue"

internal val CLS_BUNDLE_BINDING = classNameOf("$PACKAGE_NAME.internal", "BundleBinding")
internal val CLS_BUNDLER_UTILS = classNameOf(PACKAGE_NAME, "BundlerUtils")
internal val CLS_PARCEL = classNameOf("org.parceler", "Parcel")
internal val CLS_PARCELS = classNameOf("org.parceler", "Parcels")
internal val CLS_BUNDLE = classNameOf("android.os", "Bundle")
internal val CLS_PARCELABLE = classNameOf("android.os", "Parcelable")
internal val CLS_SPARSE_ARRAY = classNameOf("android.util", "SparseArray")

internal val TypeElement.measuredPackageName: String
    get() = MoreElements.getPackage(this).qualifiedName.toString()

internal fun TypeElement.getMeasuredName(suffix: String): String {
    val enclosings = mutableListOf(simpleName.toString())
    var typeElement = this
    while (typeElement.nestingKind.isNested) {
        typeElement = MoreElements.asType(typeElement.enclosingElement)
        enclosings.add(typeElement.simpleName.toString())
    }
    enclosings.reverse()
    var typeName = enclosings[0]
    for (i in 1 until enclosings.size) {
        typeName += "$${enclosings[i]}"
    }
    return typeName + suffix
}
