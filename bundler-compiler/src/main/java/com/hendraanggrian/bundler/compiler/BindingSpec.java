package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class BindingSpec extends Spec {

    @NonNull private final String packageName;
    @NonNull private final TypeMirror targetSuperclass;
    @NonNull private final TypeSpec.Builder typeSpec;
    @NonNull private final MethodSpec.Builder methodSpecGet;
    @NonNull private final MethodSpec.Builder methodSpecPut;

    BindingSpec(@NonNull TypeElement typeElement) {
        packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        targetSuperclass = typeElement.getSuperclass();
        typeSpec = TypeSpec.classBuilder(guessGeneratedName(typeElement, BindExtra.SUFFIX))
                .addModifiers(Modifier.PUBLIC);
        methodSpecGet = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(typeElement), TARGET)
                .addParameter(CLASS_BUNDLE, SOURCE);
        methodSpecPut = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(List.class, ARGS);
    }

    @NonNull
    BindingSpec superclass(@NonNull Collection<String> extraClassNames) {
        boolean extraHasSuperclass = false;
        if (targetSuperclass.getKind() != TypeKind.NONE && targetSuperclass.getKind() != TypeKind.VOID) {
            TypeElement superclass = MoreTypes.asTypeElement(targetSuperclass);
            String extraClassName = guessGeneratedName(superclass, BindExtra.SUFFIX);
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName));
                extraHasSuperclass = true;
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(CLASS_BUNDLE_BINDING);
            methodSpecGet.addStatement("super($L)", SOURCE);
        } else {
            methodSpecGet.addStatement("super($L, $L)", TARGET, SOURCE);
        }
        methodSpecPut.addStatement("super($L)", ARGS);
        return this;
    }

    @NonNull
    BindingSpec statement(@NonNull Iterable<Element> fieldElements, @NonNull Types typeUtils) {
        for (Element fieldElement : fieldElements) {
            FieldBinding field = new FieldBinding(fieldElement, typeUtils);
            if (!MoreElements.isAnnotationPresent(fieldElement, Nullable.class))
                methodSpecGet.addCode(field.checkRequiredCodeBlock());
            methodSpecGet.addCode(field.getCodeBlock());
            methodSpecPut.addCode(field.putCodeBlock());
        }
        return this;
    }

    @NonNull
    @Override
    String packageName() {
        return packageName;
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