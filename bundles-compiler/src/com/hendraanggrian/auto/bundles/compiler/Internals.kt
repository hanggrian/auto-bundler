@file:Suppress("UnstableApiUsage")

package com.hendraanggrian.auto.bundles.compiler

import com.google.auto.common.MoreElements
import com.hendraanggrian.javapoet.classOf
import javax.lang.model.element.TypeElement

internal const val FILE_COMMENT = "Bundles generated class, do not modify! https://github.com/hendraanggrian/bundles"

internal const val TARGET = "target"
internal const val SOURCE = "source"
internal const val ARGS = "args"
internal const val KEY = "key"
internal const val VALUE = "value"
internal const val DEFAULT_VALUE = "defaultValue"

internal const val PACKAGE_NAME = "com.hendraanggrian.auto.bundles"

internal val BUNDLE_BINDING = "$PACKAGE_NAME.internal".classOf("BundleBinding")
internal val BUNDLES_UTILS = PACKAGE_NAME.classOf("BundlesUtils")

internal val PARCEL = "org.parceler".classOf("Parcel")
internal val PARCELS = "org.parceler".classOf("Parcels")

internal val BUNDLE = "android.os".classOf("Bundle")
internal val PARCELABLE = "android.os".classOf("Parcelable")
internal val SPARSE_ARRAY = "android.util".classOf("SparseArray")

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
