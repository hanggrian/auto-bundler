package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import com.google.auto.common.MoreElements;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class NameUtils {

    @NonNull
    static String guessClassName(@NonNull TypeElement typeElement, @NonNull String suffix) {
        List<String> enclosings = Lists.newArrayList(typeElement.getSimpleName().toString());
        while (typeElement.getNestingKind().isNested()) {
            typeElement = MoreElements.asType(typeElement.getEnclosingElement());
            enclosings.add(typeElement.getSimpleName().toString());
        }
        Collections.reverse(enclosings);
        String typeName = enclosings.get(0);
        for (int i = 1; i < enclosings.size(); i++)
            typeName += '$' + enclosings.get(i);
        return typeName + suffix;
    }
}