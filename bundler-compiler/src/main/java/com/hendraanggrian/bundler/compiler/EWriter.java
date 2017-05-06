package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class EWriter {

    @NonNull private final TypeSpec.Builder typeSpec;
    private boolean isParcelerAvailable;

    EWriter(@NonNull Iterable<ExtraTypeSpec> extraTypes, boolean isParcelerAvailable) {
        this.typeSpec = TypeSpec.classBuilder(Name.CLASS_E.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        this.isParcelerAvailable = isParcelerAvailable;
        for (ExtraTypeSpec extraType : extraTypes)
            typeSpec.addType(extraType.toTypeSpec(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL));
        if (isParcelerAvailable) {
            TypeVariableName generic = TypeVariableName.get("T");
            Iterable<ParameterSpec> parameterSpecs = Arrays.asList(
                    ParameterSpec.builder(Name.CLASS_BUNDLE, "source")
                            .addAnnotation(NonNull.class)
                            .build(),
                    ParameterSpec.builder(String.class, "key")
                            .addAnnotation(NonNull.class)
                            .build()
            );
            typeSpec.addMethod(MethodSpec.methodBuilder("getParceler")
                    .addTypeVariable(generic)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .addParameters(parameterSpecs)
                    .addParameter(ParameterSpec.builder(generic, "defaultValue")
                            .addAnnotation(Nullable.class)
                            .build())
                    .addCode(CodeBlock.of("if (source.containsKey(key))\n" +
                            "return unwrap(source.getParcelable(key));\n"))
                    .addStatement("return defaultValue")
                    .returns(generic)
                    .build());
            typeSpec.addMethod(MethodSpec.methodBuilder("putParceler")
                    .addTypeVariable(generic)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .addParameters(parameterSpecs)
                    .addParameter(ParameterSpec.builder(generic, "value")
                            .addAnnotation(NonNull.class)
                            .build())
                    .addStatement("source.putParcelable(key, wrap(value))")
                    .build());
        }
    }

    void write(@NonNull Filer filer) throws IOException {
        JavaFile.Builder file = JavaFile.builder(Name.CLASS_E.packageName(), typeSpec.build());
        if (isParcelerAvailable) {
            file.addStaticImport(Name.CLASS_PARCELS, "unwrap");
            file.addStaticImport(Name.CLASS_PARCELS, "wrap");
        }
        file.addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                .build()
                .writeTo(filer);
    }
}