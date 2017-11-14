package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreElements
import com.squareup.javapoet.ClassName.get
import java.util.*
import javax.lang.model.element.TypeElement

internal const val TARGET = "target"
internal const val SOURCE = "source"
internal const val ARGS = "args"

internal val TYPE_BUNDLE_BINDING = get("com.hendraanggrian.bundler.internal", "BundleBinding")!!
internal val TYPE_BUNDLER_UTILS = get("com.hendraanggrian.bundler", "BundlerUtils")!!

internal val TYPE_BUNDLE = get("android.os", "Bundle")!!
internal val TYPE_PARCELABLE = get("android.os", "Parcelable")!!
internal val TYPE_SPARSE_ARRAY = get("android.util", "SparseArray")!!

internal val TYPE_PARCEL = get("org.parceler", "Parcel")!!
internal val TYPE_PARCELS = get("org.parceler", "Parcels")!!

internal fun TypeElement.getMeasuredName(suffix: String): String {
    val enclosings = mutableListOf(simpleName.toString())
    var typeElement = this
    while (typeElement.nestingKind.isNested) {
        typeElement = MoreElements.asType(typeElement.enclosingElement)
        enclosings.add(typeElement.simpleName.toString())
    }
    Collections.reverse(enclosings)
    var typeName = enclosings[0]
    for (i in 1 until enclosings.size) typeName += "$${enclosings[i]}"
    return "$typeName$suffix"
}