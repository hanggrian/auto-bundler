package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.hendraanggrian.bundler.BindExtra;
import com.hendraanggrian.bundler.BindState;
import com.squareup.javapoet.CodeBlock;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class FieldBinding {

    @NonNull private final ExtraType type;
    @NonNull private final Name name;
    @NonNull private final Annotation annotation;
    @NonNull private final String key;

    FieldBinding(@NonNull Element fieldElement, @NonNull Types typeUtils) {
        this.type = ExtraType.valueOf(fieldElement, typeUtils);
        this.name = fieldElement.getSimpleName();
        BindExtra bindExtra = fieldElement.getAnnotation(BindExtra.class);
        BindState bindState = fieldElement.getAnnotation(BindState.class);
        if (bindExtra != null && bindState != null) {
            throw new IllegalStateException(name.toString() + " is annotated with BindExtra and BindState, this is unsupported behavior.");
        } else if (bindExtra != null) {
            annotation = bindExtra;
            String key = bindExtra.value();
            this.key = !key.isEmpty() ? key : name.toString();
        } else if (bindState != null) {
            annotation = bindState;
            String key = bindState.value();
            this.key = !key.isEmpty() ? key : name.toString();
        } else {
            throw new IllegalStateException("Couldn't read key from " + name.toString());
        }
    }

    boolean isBindExtra() {
        return annotation instanceof BindExtra;
    }

    @NonNull
    CodeBlock checkRequiredCodeBlock() {
        return CodeBlock.of("checkRequired($S, $S);\n", key, name);
    }

    @NonNull
    CodeBlock getCodeBlock() {
        if (type != ExtraType.PARCELER)
            return CodeBlock.of("$L.$L = $L($S, $L.$L);\n",
                    Spec.TARGET, name, type.getMethodName(), key, Spec.TARGET, name);
        else
            return CodeBlock.of("$L.$L = $T.getParceler($L, $S, $L.$L);\n",
                    Spec.TARGET, name, Spec.CLASS_BUNDLER_UTILS, Spec.SOURCE, key, Spec.TARGET, name);
    }

    @NonNull
    CodeBlock putCodeBlock() {
        if (type != ExtraType.PARCELER)
            return CodeBlock.of("if(!$L.isEmpty()) $L.$L($S, ($L) nextArg());\n",
                    Spec.ARGS, Spec.SOURCE, type.putMethodName(), key, type.typeName.toString());
        else
            return CodeBlock.of("if(!$L.isEmpty()) $T.putParceler($L, $S, nextArg());\n",
                    Spec.ARGS, Spec.CLASS_BUNDLER_UTILS, Spec.SOURCE, key);
    }
}