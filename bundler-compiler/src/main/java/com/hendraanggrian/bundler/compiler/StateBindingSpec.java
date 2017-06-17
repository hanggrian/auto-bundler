package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.auto.common.MoreTypes;
import com.hendraanggrian.bundler.BindState;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.Arrays;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class StateBindingSpec extends BindingSpec {

    @NonNull private final MethodSpec.Builder constructorBinding;
    @NonNull private final MethodSpec.Builder constructorSaving;

    StateBindingSpec(@NonNull TypeElement typeElement) {
        super(typeElement, BindState.SUFFIX);
        this.constructorBinding = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(typeElement), TARGET)
                .addParameter(CLASS_BUNDLE, SOURCE);
        this.constructorSaving = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_BUNDLE, SOURCE)
                .addParameter(ClassName.get(typeElement), TARGET);
    }

    @NonNull
    @Override
    StateBindingSpec superclass(@NonNull Collection<String> extraClassNames) {
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
            constructorSaving.addStatement("super($L)", SOURCE);
        } else {
            constructorBinding.addStatement("super($L, $L)", TARGET, SOURCE);
            constructorSaving.addStatement("super($L, $L)", SOURCE, TARGET);
        }
        return this;
    }

    @NonNull
    @Override
    StateBindingSpec statement(@NonNull Iterable<Element> fieldElements, @NonNull Types typeUtils) {
        for (Element fieldElement : fieldElements) {
            FieldBinding field = new FieldBinding(fieldElement, typeUtils);
            constructorBinding.addCode(field.getCodeBlock());
            constructorSaving.addCode(field.putCodeBlock());
        }
        return this;
    }

    @NonNull
    @Override
    Iterable<MethodSpec.Builder> methodSpecs() {
        return Arrays.asList(constructorBinding, constructorSaving);
    }
}