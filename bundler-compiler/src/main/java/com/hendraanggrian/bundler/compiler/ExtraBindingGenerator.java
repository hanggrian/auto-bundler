package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.RParser;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.BindExtraRes;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class ExtraBindingGenerator implements Generator {

    private static final TypeName TYPE_BINDING = ClassName.get("com.hendraanggrian.bundler", "ExtraBinding");
    private static final ParameterSpec PARAM_SOURCE = ParameterSpec.builder(ClassName.get("android.os", "Bundle"), "source").build();
    private static final ParameterSpec PARAM_RES = ParameterSpec.builder(ClassName.get("android.content.res", "Resources"), "res").build();

    @NonNull private final String packageName;
    @NonNull private final ClassName className;
    @NonNull private final TypeMirror superclass;
    @NonNull private final TypeSpec.Builder type;
    @NonNull private final MethodSpec.Builder methodConstructor;

    ExtraBindingGenerator(@NonNull TypeElement typeElement) {
        packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        className = ClassName.get(typeElement);
        superclass = typeElement.getSuperclass();
        type = TypeSpec.classBuilder(NameUtils.guessClassName(typeElement))
                .addModifiers(Modifier.PUBLIC);
        methodConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className, "target")
                .addParameter(PARAM_SOURCE)
                .addParameter(PARAM_RES);
    }

    @NonNull
    ExtraBindingGenerator superclass(@NonNull Collection<String> generatedClassNames) {
        boolean hasSuperclass = false;
        if (superclass.getKind() != TypeKind.NONE && superclass.getKind() != TypeKind.VOID) {
            String className = NameUtils.guessClassName(MoreTypes.asTypeElement(superclass));
            if (generatedClassNames.contains(className)) {
                type.superclass(ClassName.get(packageName, className));
                hasSuperclass = true;
            }
        }
        if (!hasSuperclass) {
            type.superclass(TYPE_BINDING);
            methodConstructor.addStatement("super($L, $L)", PARAM_SOURCE.name, PARAM_RES.name);
        } else {
            methodConstructor.addStatement("super(target, $L, $L)", PARAM_SOURCE.name, PARAM_RES.name);
        }
        return this;
    }

    @NonNull
    ExtraBindingGenerator statement(@NonNull RParser parser, @NonNull Iterable<Element> fieldElements) {
        for (Element fieldElement : fieldElements) {
            String field = fieldElement.getSimpleName().toString();
            BindExtra bindPref = fieldElement.getAnnotation(BindExtra.class);
            String key = bindPref != null
                    ? "\"" + (!bindPref.value().isEmpty() ? bindPref.value() : field) + "\""
                    : parser.parse(packageName, fieldElement.getAnnotation(BindExtraRes.class).value());
            boolean required = fieldElement.getAnnotation(Nullable.class) == null;
            String targetName = className.simpleName() + "#" + field;
            methodConstructor.addStatement("target.$L = getValue($L, target.$L, $L, $S)", field, key, field, required, targetName);
        }
        return this;
    }

    @Override
    public void generate(@NonNull Filer filer) throws IOException {
        JavaFile.builder(packageName, type
                .addMethod(methodConstructor.build())
                .build())
                .addFileComment("TODO: go drink some water, Bundler got this covered.")
                .build()
                .writeTo(filer);
    }
}