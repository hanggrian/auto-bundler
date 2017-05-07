package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.hendraanggrian.bundler.annotations.BindExtra;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class ExtraFieldBinding {

    @NonNull private final ExtraType type;
    @NonNull private final Name name;
    @NonNull private final String key;

    ExtraFieldBinding(@NonNull Element fieldElement, @NonNull Types typeUtils) {
        this.type = ExtraType.valueOf(fieldElement, typeUtils);
        this.name = fieldElement.getSimpleName();
        String key = fieldElement.getAnnotation(BindExtra.class).value();
        this.key = !key.isEmpty() ? key : name.toString();
    }

    @NonNull
    CodeBlock checkRequiredStatement() {
        return CodeBlock.of("checkRequired($S, $S);\n", key, name);
    }

    @NonNull
    CodeBlock getStatement() {
        if (type != ExtraType.PARCELER)
            return CodeBlock.of("$L.$L = $L($S, $L.$L);\n",
                    Names.TARGET, name, type.getMethodName(), key, Names.TARGET, name);
        else
            return CodeBlock.of("$L.$L = $T.getParceler($L, $S, $L.$L);\n",
                    Names.TARGET, name, Names.CLASS_BUNDLER_UTILS, Names.SOURCE, key, Names.TARGET, name);
    }

    @NonNull
    CodeBlock putStatement() {
        if (type != ExtraType.PARCELER)
            return CodeBlock.of("if(!$L.isEmpty()) $L.$L($S, ($L) nextArg());\n",
                    Names.ARGS, Names.SOURCE, type.putMethodName(), key, type.typeName.toString());
        else
            return CodeBlock.of("if(!$L.isEmpty()) $T.putParceler($L, $S, nextArg());\n",
                    Names.ARGS, Names.CLASS_BUNDLER_UTILS, Names.SOURCE, key);
    }
}