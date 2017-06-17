package com.hendraanggrian.bundler.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hendraanggrian.bundler.BindExtra;
import com.hendraanggrian.bundler.BindState;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@AutoService(Processor.class)
public final class BundlerProcessor extends AbstractProcessor {

    private static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = ImmutableSet.<Class<? extends Annotation>>of(
            BindExtra.class,
            BindState.class
    );

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> names = Sets.newLinkedHashSet();
        for (Class<? extends Annotation> cls : SUPPORTED_ANNOTATIONS) {
            names.add(cls.getCanonicalName());
        }
        return names;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        // build utility class if parceler is available and if it has not yet already been created
        if (elementUtils.getTypeElement(Spec.CLASS_PARCELS.toString()) != null &&
                elementUtils.getTypeElement(Spec.CLASS_BUNDLER_UTILS.toString()) == null) {
            JavaFile file = new UtilsSpec().toJavaFile();
            try {
                file.writeTo(filer);
            } catch (IOException ignored) {
            }
        }
        // preparing elements
        for (Class<? extends Annotation> annotation : SUPPORTED_ANNOTATIONS) {
            String suffix = annotation == BindExtra.class
                    ? BindExtra.SUFFIX
                    : BindState.SUFFIX;
            Multimap<TypeElement, Element> map = LinkedHashMultimap.create();
            Set<String> generatedClassNames = Sets.newHashSet();
            for (Element fieldElement : roundEnv.getElementsAnnotatedWith(annotation)) {
                TypeElement typeElement = MoreElements.asType(fieldElement.getEnclosingElement());
                map.put(typeElement, fieldElement);
                generatedClassNames.add(Spec.guessGeneratedName(typeElement, suffix));
            }
            // write classes and keep results
            for (TypeElement key : map.keySet()) {
                JavaFile file = new BindingSpec(key, suffix)
                        .superclass(generatedClassNames)
                        .statement(map.get(key), typeUtils)
                        .toJavaFile();
                try {
                    file.writeTo(filer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
}