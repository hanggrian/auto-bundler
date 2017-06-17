package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.TypeSpec;

import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class BindingSpec extends Spec {

    @NonNull final String suffix;
    @NonNull final String packageName;
    @NonNull final TypeMirror targetSuperclass;
    @NonNull final TypeSpec.Builder typeSpec;

    BindingSpec(@NonNull TypeElement typeElement, @NonNull String suffix) {
        this.suffix = suffix;
        this.packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        this.targetSuperclass = typeElement.getSuperclass();
        this.typeSpec = TypeSpec.classBuilder(guessGeneratedName(typeElement, suffix))
                .addModifiers(Modifier.PUBLIC);
    }

    @NonNull
    abstract BindingSpec superclass(@NonNull Collection<String> extraClassNames);

    @NonNull
    abstract BindingSpec statement(@NonNull Iterable<Element> fieldElements, @NonNull Types typeUtils);

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
}