package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.WrapExtras;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
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
final class Generator {

    @NonNull private final String packageName;
    @NonNull private final ClassName targetClassName;
    @NonNull private final TypeMirror targetSuperclass;
    @NonNull private final TypeSpec.Builder bindingType;
    @NonNull private final MethodSpec.Builder bindingConstructor;
    @Nullable private TypeSpec.Builder wrappingType;
    @Nullable private MethodSpec.Builder wrappingConstructor;

    Generator(@NonNull TypeElement typeElement) {
        packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();

        targetClassName = ClassName.get(typeElement);
        targetSuperclass = typeElement.getSuperclass();
        bindingType = TypeSpec.classBuilder(NameUtils.guessClassName(typeElement, BindExtra.SUFFIX))
                .addModifiers(Modifier.PUBLIC);
        bindingConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetClassName, "target")
                .addParameter(ParameterSpec.builder(ClassName.get("android.os", "Bundle"), "source").build());

        if (typeElement.getAnnotation(WrapExtras.class) != null) {
            wrappingType = TypeSpec.classBuilder(NameUtils.guessClassName(typeElement, WrapExtras.SUFFIX))
                    .addModifiers(Modifier.PUBLIC);
            wrappingConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(List.class), "args");
        }
    }

    @NonNull
    Generator superclass(@NonNull Collection<String> extraClassNames, @NonNull Collection<String> extrasClassNames) {
        boolean extraHasSuperclass = false;
        boolean extrasHasSuperclass = false;
        if (targetSuperclass.getKind() != TypeKind.NONE && targetSuperclass.getKind() != TypeKind.VOID) {
            TypeElement superclass = MoreTypes.asTypeElement(targetSuperclass);
            String extraClassName = NameUtils.guessClassName(superclass, BindExtra.SUFFIX);
            if (extraClassNames.contains(extraClassName)) {
                bindingType.superclass(ClassName.get(packageName, extraClassName));
                extraHasSuperclass = true;
            }
            String extrasClassName = NameUtils.guessClassName(superclass, WrapExtras.SUFFIX);
            if (extrasClassNames.contains(extrasClassName)) {
                if (wrappingType != null)
                    wrappingType.superclass(ClassName.get(packageName, extrasClassName));
                extrasHasSuperclass = true;
            }
        }
        if (!extraHasSuperclass) {
            bindingType.superclass(ClassName.get("com.hendraanggrian.bundler", "ExtraBinding"));
            bindingConstructor.addStatement("super(source)");
        } else {
            bindingConstructor.addStatement("super(target, source)");
        }
        if (!extrasHasSuperclass) {
            if (wrappingType != null)
                wrappingType.superclass(ClassName.get("com.hendraanggrian.bundler", "ExtrasWrapping"));
            if (wrappingConstructor != null)
                wrappingConstructor.addStatement("super(args)");
        } else {
            if (wrappingConstructor != null)
                wrappingConstructor.addStatement("super(args)");
        }
        return this;
    }

    @NonNull
    Generator statement(@NonNull Types typeUtils, @NonNull Iterable<Element> fieldElements) {
        for (Element fieldElement : fieldElements) {
            ExtraType type = ExtraType.valueOf(typeUtils, fieldElement);
            String field = fieldElement.getSimpleName().toString();
            String key = fieldElement.getAnnotation(BindExtra.class).value();
            if (key.isEmpty())
                key = field;
            if (fieldElement.getAnnotation(Nullable.class) == null) {
                bindingConstructor.addStatement("checkRequired($S, $S)", key, targetClassName.simpleName() + "#" + field);
            }
            bindingConstructor.addStatement("target.$L = $L($S, target.$L)", field, type.getMethodName(), key, field);
            if (wrappingConstructor != null) {
                if (type == ExtraType.PARCELER)
                    wrappingConstructor.addStatement("if(!args.isEmpty()) source.$L($S, wrap(next()))", type.putMethodName(), key);
                else
                    wrappingConstructor.addStatement("if(!args.isEmpty()) source.$L($S, ($L) next())", type.putMethodName(), key, fieldElement.asType());
            }
        }
        return this;
    }

    void generate(@NonNull Filer filer) throws IOException {
        JavaFile.builder(packageName, bindingType
                .addMethod(bindingConstructor.build())
                .build())
                .addFileComment("TODO: go drink some water, Bundler got this covered.")
                .build()
                .writeTo(filer);
        if (wrappingType != null && wrappingConstructor != null) {
            JavaFile.builder(packageName, wrappingType
                    .addMethod(wrappingConstructor.build())
                    .build())
                    .addFileComment("TODO: go drink some water, Bundler got this covered.")
                    .addStaticImport(ClassName.get("org.parceler", "Parcels"), "wrap")
                    .build()
                    .writeTo(filer);
        }
    }
}