package com.hendraanggrian.bundler.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hendraanggrian.RParser;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.BindExtraRes;

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

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@AutoService(Processor.class)
public final class BundlerProcessor extends AbstractProcessor {

    private static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = ImmutableSet.of(BindExtra.class, BindExtraRes.class);

    private Filer filer;
    private RParser parser;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> names = Sets.newHashSet();
        for (Class<? extends Annotation> cls : SUPPORTED_ANNOTATIONS)
            names.add(cls.getCanonicalName());
        return names;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        parser = RParser.builder(processingEnv)
                .setSupportedAnnotations(SUPPORTED_ANNOTATIONS)
                .setSupportedTypes("string")
                .build();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        // preparing R classes
        parser.scan(roundEnv);
        // preparing elements
        Multimap<TypeElement, Element> map = LinkedHashMultimap.create();
        Set<String> validClassNames = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : SUPPORTED_ANNOTATIONS) {
            for (Element fieldElement : roundEnv.getElementsAnnotatedWith(annotation)) {
                TypeElement typeElement = MoreElements.asType(fieldElement.getEnclosingElement());
                map.put(typeElement, fieldElement);
                validClassNames.add(NameUtils.guessClassName(typeElement));
            }
        }
        // generate classes
        for (TypeElement key : map.keySet()) {
            Generator generator = new ExtraBindingGenerator(key)
                    .superclass(validClassNames)
                    .statement(parser, map.get(key));
            try {
                generator.generate(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}