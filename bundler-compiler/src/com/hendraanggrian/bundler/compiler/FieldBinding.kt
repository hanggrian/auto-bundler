package com.hendraanggrian.bundler.compiler

import com.hendraanggrian.bundler.BindExtra
import com.hendraanggrian.bundler.BindState
import com.squareup.javapoet.CodeBlock
import javax.lang.model.element.Element
import javax.lang.model.util.Types

internal class FieldBinding(fieldElement: Element, typeUtils: Types) {

    private val type = BundleValueType.valueOf(fieldElement, typeUtils)
    private val name = fieldElement.simpleName
    private val annotation: Annotation
    private val key: String

    val isBindExtra: Boolean get() = annotation is BindExtra

    val codeBlock: CodeBlock
        get() = when {
            type != BundleValueType.PARCELER -> CodeBlock.of(
                "\$L.\$L = \$L(\$S, \$L.\$L)",
                TARGET, name, type.getMethodName, key, TARGET, name
            )
            else -> CodeBlock.of(
                "\$L.\$L = \$T.getParceler(\$L, \$S, \$L.\$L)",
                TARGET, name, BUNDLER_UTILS, SOURCE, key, TARGET, name
            )
        }

    init {
        val extra = fieldElement.getAnnotation(BindExtra::class.java)
        val state = fieldElement.getAnnotation(BindState::class.java)
        if (extra != null && state != null) {
            error(
                "$name is annotated with Extra and State, " +
                    "this is unsupported behavior."
            )
        } else if (extra != null) {
            annotation = extra
            val key = extra.value
            this.key = if (key.isNotEmpty()) key else name.toString()
        } else if (state != null) {
            annotation = state
            val key = state.value
            this.key = if (key.isNotEmpty()) key else name.toString()
        } else {
            throw IllegalStateException("Couldn't read key from $name")
        }
    }

    val checkRequiredCodeBlock: CodeBlock
        get() = CodeBlock.of("checkRequired(\$S, \$S)", key, name)

    val putCodeBlockWithList: CodeBlock
        get() = when {
            type != BundleValueType.PARCELER -> CodeBlock.of(
                "if (!\$L.isEmpty()) getSource().\$L(\$S, (\$L) nextArg())",
                ARGS, type.putMethodName, key, type.typeName.toString()
            )
            else -> CodeBlock.of(
                "if (!\$L.isEmpty()) \$T.putParceler(getSource(), \$S, nextArg())",
                ARGS, BUNDLER_UTILS, key
            )
        }

    val putCodeBlock: CodeBlock
        get() = when {
            type != BundleValueType.PARCELER -> CodeBlock.of(
                "\$L.\$L(\$S, \$L.\$L)",
                SOURCE, type.putMethodName, key, TARGET, name
            )
            else -> CodeBlock.of(
                "\$T.putParceler(\$L, \$S, \$L.\$L)",
                BUNDLER_UTILS, SOURCE, key, TARGET, name
            )
        }
}
