package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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
final class BindingSet {

    @NonNull private final String packageName;
    @NonNull private final ClassName targetClassName;
    @NonNull private final TypeMirror targetSuperclass;
    @NonNull private final TypeSpec.Builder typeSpec;
    @NonNull private final MethodSpec.Builder methodSpecGet;
    @NonNull private final MethodSpec.Builder methodSpecPut;

    BindingSet(@NonNull TypeElement typeElement) {
        packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        targetClassName = ClassName.get(typeElement);
        targetSuperclass = typeElement.getSuperclass();
        typeSpec = TypeSpec.classBuilder(Names.guessGeneratedName(typeElement, BindExtra.SUFFIX))
                .addModifiers(Modifier.PUBLIC);
        methodSpecGet = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetClassName, Names.TARGET)
                .addParameter(Names.CLASS_BUNDLE, Names.SOURCE);
        methodSpecPut = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(List.class, Names.ARGS);
    }

    @NonNull
    BindingSet superclass(@NonNull Collection<String> extraClassNames) {
        boolean extraHasSuperclass = false;
        if (targetSuperclass.getKind() != TypeKind.NONE && targetSuperclass.getKind() != TypeKind.VOID) {
            TypeElement superclass = MoreTypes.asTypeElement(targetSuperclass);
            String extraClassName = Names.guessGeneratedName(superclass, BindExtra.SUFFIX);
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName));
                extraHasSuperclass = true;
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(Names.CLASS_BUNDLE_BINDING);
            methodSpecGet.addStatement("super($L)", Names.SOURCE);
        } else {
            methodSpecGet.addStatement("super($L, $L)", Names.TARGET, Names.SOURCE);
        }
        methodSpecPut.addStatement("super($L)", Names.ARGS);
        return this;
    }

    @NonNull
    BindingSet statement(@NonNull Iterable<Element> fieldElements, @NonNull Types typeUtils) {
        for (Element fieldElement : fieldElements) {
            ExtraFieldBinding field = new ExtraFieldBinding(fieldElement, typeUtils);
            if (!MoreElements.isAnnotationPresent(fieldElement, Nullable.class))
                methodSpecGet.addCode(field.checkRequiredStatement());
            methodSpecGet.addCode(field.getStatement());
            methodSpecPut.addCode(field.putStatement());
        }
        return this;
    }

    @NonNull
    JavaFile toJavaFile() {
        return JavaFile.builder(packageName, typeSpec
                .addMethod(methodSpecGet.build())
                .addMethod(methodSpecPut.build())
                .build())
                .addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                .build();
    }
}