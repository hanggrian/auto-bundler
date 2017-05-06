package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class BindingWriter {

    @NonNull private final String packageName;
    @NonNull private final ClassName targetClassName;
    @NonNull private final TypeMirror targetSuperclass;
    @NonNull private final TypeSpec.Builder typeSpec;
    @NonNull private final MethodSpec.Builder getConstructor;
    @NonNull private final MethodSpec.Builder putConstructor;
    @NonNull private final ExtraTypeSpec extraType;

    BindingWriter(@NonNull TypeElement typeElement) {
        packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        targetClassName = ClassName.get(typeElement);
        targetSuperclass = typeElement.getSuperclass();
        typeSpec = TypeSpec.classBuilder(Name.guessGeneratedName(typeElement, BindExtra.SUFFIX))
                .addModifiers(Modifier.PUBLIC);
        getConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetClassName, Name.TARGET)
                .addParameter(ParameterSpec.builder(Name.CLASS_BUNDLE, Name.SOURCE)
                        .build());
        putConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(List.class), Name.ARGS);
        extraType = new ExtraTypeSpec(typeElement.getSimpleName());
    }

    @NonNull
    BindingWriter superclass(@NonNull Collection<String> extraClassNames) {
        boolean extraHasSuperclass = false;
        if (targetSuperclass.getKind() != TypeKind.NONE && targetSuperclass.getKind() != TypeKind.VOID) {
            TypeElement superclass = MoreTypes.asTypeElement(targetSuperclass);
            String extraClassName = Name.guessGeneratedName(superclass, BindExtra.SUFFIX);
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName));
                extraHasSuperclass = true;
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(Name.CLASS_EXTRA_BINDING);
            getConstructor.addStatement("super($L)", Name.SOURCE);
        } else {
            getConstructor.addStatement("super($L, $L)", Name.TARGET, Name.SOURCE);
        }
        putConstructor.addStatement("super($L)", Name.ARGS);
        return this;
    }

    @NonNull
    BindingWriter statement(@NonNull Types typeUtils, @NonNull Iterable<Element> fieldElements) {
        for (Element fieldElement : fieldElements) {
            ExtraType type = ExtraType.valueOf(typeUtils, fieldElement);
            String field = fieldElement.getSimpleName().toString();
            String key = fieldElement.getAnnotation(BindExtra.class).value();
            if (key.isEmpty())
                key = field;
            if (!MoreElements.isAnnotationPresent(fieldElement, Nullable.class))
                getConstructor.addStatement("checkRequired($S, $S)", key, targetClassName.simpleName() + "#" + field);
            if (type != ExtraType.PARCELER) {
                getConstructor.addStatement("$L.$L = $L($S, $L.$L)",
                        Name.TARGET, field, type.getMethodName(), key, Name.TARGET, field);
                putConstructor.addStatement("if(!$L.isEmpty()) $L.$L($S, ($L) $L.get(0))",
                        Name.ARGS, Name.SOURCE, type.putMethodName(), key, fieldElement.asType(), Name.ARGS);
            } else {
                getConstructor.addStatement("$L.$L = $T.getParceler($L, $S, $L.$L)",
                        Name.TARGET, field, Name.CLASS_E, Name.SOURCE, key, Name.TARGET, field);
                putConstructor.addStatement("if(!$L.isEmpty()) $T.putParceler($L, $S, $L.get(0))",
                        Name.ARGS, Name.CLASS_E, Name.SOURCE, key, Name.ARGS);
            }
            extraType.put(field, key);
        }
        return this;
    }

    @NonNull
    ExtraTypeSpec write(@NonNull Filer filer) throws IOException {
        JavaFile.builder(packageName, typeSpec
                .addMethod(getConstructor.build())
                .addMethod(putConstructor.build())
                .build())
                .addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                .build()
                .writeTo(filer);
        return extraType;
    }
}