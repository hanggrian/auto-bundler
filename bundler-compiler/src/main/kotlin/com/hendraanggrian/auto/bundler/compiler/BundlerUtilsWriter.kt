package com.hendraanggrian.auto.bundler.compiler

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.hendraanggrian.javapoet.MethodSpecBuilder
import com.hendraanggrian.javapoet.buildJavaFile
import com.hendraanggrian.javapoet.collections.MethodSpecListScope
import com.hendraanggrian.javapoet.genericsBy
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

private val GENERIC = "T".genericsBy()

internal fun Filer.writeBundlerUtils() = buildJavaFile(PACKAGE_NAME) {
    comment = FILE_COMMENT
    addClass(CLS_BUNDLER_UTILS.simpleName()) {
        addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        methods {
            addConstructor {
                addModifiers(Modifier.PRIVATE)
            }
            parcelerParameter("getParceler", REF_DEFAULT_VALUE) {
                append(
                    "if ($REF_SOURCE.containsKey($REF_KEY))\n" +
                        "return \$$GENERIC.unwrap($REF_SOURCE.getParcelable($REF_KEY));\n",
                    CLS_PARCELS
                )
                appendLine("return $REF_DEFAULT_VALUE")
                returns = GENERIC
            }
            parcelerParameter("putParceler", REF_VALUE) {
                appendLine(
                    "$REF_SOURCE.putParcelable($REF_KEY, \$$GENERIC.wrap($REF_VALUE))",
                    CLS_PARCELS
                )
            }
        }
    }
}.writeTo(this)

private fun MethodSpecListScope.parcelerParameter(
    name: String,
    genericParamName: String,
    builderAction: MethodSpecBuilder.() -> Unit
) = add(name) {
    addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
    typeVariables += GENERIC
    parameters {
        add(CLS_BUNDLE, REF_SOURCE) { annotations.add<NonNull>() }
        add<String>(REF_KEY) { annotations.add<NonNull>() }
        add(GENERIC, genericParamName) { annotations.add<Nullable>() }
    }
    builderAction()
}
