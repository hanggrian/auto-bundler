package com.hendraanggrian.bundler.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.WrapExtras;

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
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@AutoService(Processor.class)
public final class BundlerProcessor extends AbstractProcessor {

    private static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = ImmutableSet.of(
            BindExtra.class,
            WrapExtras.class
    );

    private Types typeUtils;
    private Filer filer;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> names = Sets.newLinkedHashSet();
        for (Class<? extends Annotation> cls : SUPPORTED_ANNOTATIONS)
            names.add(cls.getCanonicalName());
        return names;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        // preparing elements
        Multimap<TypeElement, Element> map = LinkedHashMultimap.create();
        Set<String> extraClassNames = Sets.newHashSet();
        Set<String> extrasClassNames = Sets.newHashSet();
        for (Element fieldElement : roundEnv.getElementsAnnotatedWith(BindExtra.class)) {
            TypeElement typeElement = MoreElements.asType(fieldElement.getEnclosingElement());
            map.put(typeElement, fieldElement);
            extraClassNames.add(Generator.guessExtraClassName(typeElement));
            extrasClassNames.add(Generator.guessExtrasClassName(typeElement));
        }
        // generate classes and keep results
        for (TypeElement key : map.keySet()) {
            Generator generator = new Generator(key)
                    .superclass(extraClassNames, extrasClassNames)
                    .statement(typeUtils, map.get(key));
            try {
                generator.generate(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}