package com.hendraanggrian.bundler.compiler

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.hendraanggrian.javapoet.MethodSpecBuilder
import com.hendraanggrian.javapoet.buildJavaFile
import com.hendraanggrian.javapoet.collections.MethodSpecListScope
import com.squareup.javapoet.TypeVariableName
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

private val GENERIC = TypeVariableName.get("T")

internal fun Filer.writeBundlerUtils() = buildJavaFile(PACKAGE_NAME) {
    comment = FILE_COMMENT
    addClass(BUNDLER_UTILS.simpleName()) {
        addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        methods {
            addConstructor {
                addModifiers(Modifier.PRIVATE)
            }
            parcelerParameter("getParceler", DEFAULT_VALUE) {
                append(
                    "if ($SOURCE.containsKey($KEY))\n" +
                        "return \$$GENERIC.unwrap($SOURCE.getParcelable($KEY));\n", PARCELS
                )
                appendln("return $DEFAULT_VALUE")
                returns = GENERIC
            }
            parcelerParameter("putParceler", VALUE) {
                appendln("$SOURCE.putParcelable($KEY, \$$GENERIC.wrap($VALUE))", PARCELS)
            }
        }
    }
}.writeTo(this)

private inline fun MethodSpecListScope.parcelerParameter(
    name: String,
    genericParamName: String,
    builderAction: MethodSpecBuilder.() -> Unit
) = add(name) {
    addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
    typeVariables += GENERIC
    parameters {
        add(BUNDLE, SOURCE) { annotations.add<NonNull>() }
        add<String>(KEY) { annotations.add<NonNull>() }
        add(GENERIC, genericParamName) { annotations.add<Nullable>() }
    }
    builderAction()
}
