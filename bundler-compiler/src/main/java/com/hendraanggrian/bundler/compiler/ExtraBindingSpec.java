package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.BindExtra;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class ExtraBindingSpec extends BindingSpec {

    @NonNull private final MethodSpec.Builder constructorBinding;
    @NonNull private final MethodSpec.Builder constructorWrapping;

    ExtraBindingSpec(@NonNull TypeElement typeElement) {
        super(typeElement, BindExtra.SUFFIX);
        this.constructorBinding = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(typeElement), TARGET)
                .addParameter(CLASS_BUNDLE, SOURCE);
        this.constructorWrapping = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(List.class, ARGS);
    }

    @NonNull
    @Override
    ExtraBindingSpec superclass(@NonNull Collection<String> extraClassNames) {
        boolean extraHasSuperclass = false;
        if (targetSuperclass.getKind() != TypeKind.NONE && targetSuperclass.getKind() != TypeKind.VOID) {
            TypeElement superclass = MoreTypes.asTypeElement(targetSuperclass);
            String extraClassName = guessGeneratedName(superclass, suffix);
            if (extraClassNames.contains(extraClassName)) {
                typeSpec.superclass(ClassName.get(packageName, extraClassName));
                extraHasSuperclass = true;
            }
        }
        if (!extraHasSuperclass) {
            typeSpec.superclass(CLASS_BUNDLE_BINDING);
            constructorBinding.addStatement("super($L)", SOURCE);
        } else {
            constructorBinding.addStatement("super($L, $L)", TARGET, SOURCE);
        }
        constructorWrapping.addStatement("super($L)", ARGS);
        return this;
    }

    @NonNull
    @Override
    ExtraBindingSpec statement(@NonNull Iterable<Element> fieldElements, @NonNull Types typeUtils) {
        for (Element fieldElement : fieldElements) {
            FieldBinding field = new FieldBinding(fieldElement, typeUtils);
            if (!MoreElements.isAnnotationPresent(fieldElement, Nullable.class)) {
                constructorBinding.addCode(field.checkRequiredCodeBlock());
            }
            constructorBinding.addCode(field.getCodeBlock());
            constructorWrapping.addCode(field.putCodeBlockWithList());
        }
        return this;
    }

    @NonNull
    @Override
    Iterable<MethodSpec.Builder> methodSpecs() {
        return Arrays.asList(constructorBinding, constructorWrapping);
    }
}