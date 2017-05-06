package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.common.collect.Maps;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class ExtraTypeSpec {

    @NonNull private final String clsName;
    @NonNull private final Map<String, String> fieldKeyMap;

    ExtraTypeSpec(@NonNull Name clsName) {
        this.clsName = clsName.toString();
        this.fieldKeyMap = Maps.newLinkedHashMap();
    }

    void put(@NonNull String field, @NonNull String key) {
        fieldKeyMap.put(field, key);
    }

    @NonNull
    TypeSpec toTypeSpec(@NonNull Modifier... modifiers) {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(clsName)
                .addModifiers(modifiers);
        for (String field : fieldKeyMap.keySet())
            typeSpec.addField(FieldSpec.builder(String.class, field, modifiers)
                    .initializer("$S", fieldKeyMap.get(field))
                    .build());
        return typeSpec.build();
    }
}