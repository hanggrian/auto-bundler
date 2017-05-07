package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.Arrays;

import javax.lang.model.element.Modifier;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class UtilsSpec extends Spec {

    @NonNull private final TypeSpec.Builder typeSpec;
    @NonNull private final MethodSpec.Builder methodSpecGet;
    @NonNull private final MethodSpec.Builder methodSpecPut;

    UtilsSpec() {
        TypeVariableName generic = TypeVariableName.get("T");
        Iterable<ParameterSpec> parameterSpecs = Arrays.asList(
                ParameterSpec.builder(CLASS_BUNDLE, "source")
                        .addAnnotation(NonNull.class)
                        .build(),
                ParameterSpec.builder(String.class, "key")
                        .addAnnotation(NonNull.class)
                        .build()
        );
        this.typeSpec = TypeSpec.classBuilder(CLASS_BUNDLER_UTILS.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        this.methodSpecGet = MethodSpec.methodBuilder("getParceler")
                .addTypeVariable(generic)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameters(parameterSpecs)
                .addParameter(ParameterSpec.builder(generic, "defaultValue")
                        .addAnnotation(Nullable.class)
                        .build())
                .addCode(CodeBlock.of("if (source.containsKey(key))\n" +
                        "return $T.unwrap(source.getParcelable(key));\n", CLASS_PARCELS))
                .addStatement("return defaultValue")
                .returns(generic);
        this.methodSpecPut = MethodSpec.methodBuilder("putParceler")
                .addTypeVariable(generic)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameters(parameterSpecs)
                .addParameter(ParameterSpec.builder(generic, "value")
                        .addAnnotation(NonNull.class)
                        .build())
                .addStatement("source.putParcelable(key, $T.wrap(value))", CLASS_PARCELS);
    }

    @NonNull
    @Override
    String packageName() {
        return "com.hendraanggrian.bundler";
    }

    @NonNull
    @Override
    TypeSpec.Builder typeSpec() {
        return typeSpec;
    }

    @NonNull
    @Override
    Iterable<MethodSpec.Builder> methodSpecs() {
        return Arrays.asList(methodSpecGet, methodSpecPut);
    }
}