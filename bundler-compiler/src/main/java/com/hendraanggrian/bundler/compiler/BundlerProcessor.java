package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@AutoService(Processor.class)
public final class BundlerProcessor extends AbstractProcessor {

    private static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = ImmutableSet.<Class<? extends Annotation>>of(
            BindExtra.class
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
        for (Class<? extends Annotation> cls : SUPPORTED_ANNOTATIONS)
            names.add(cls.getCanonicalName());
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
        // preparing elements
        Multimap<TypeElement, Element> map = LinkedHashMultimap.create();
        Set<String> extraClassNames = Sets.newHashSet();
        for (Element fieldElement : roundEnv.getElementsAnnotatedWith(BindExtra.class)) {
            TypeElement typeElement = MoreElements.asType(fieldElement.getEnclosingElement());
            map.put(typeElement, fieldElement);
            extraClassNames.add(Names.guessGeneratedName(typeElement, BindExtra.SUFFIX));
        }
        // write classes and keep results
        for (TypeElement key : map.keySet()) {
            try {
                new BindingSet(key)
                        .superclass(extraClassNames)
                        .statement(map.get(key), typeUtils)
                        .toJavaFile()
                        .writeTo(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!isParcelerAvailable())
            return false;
        // build utility class
        TypeVariableName generic = TypeVariableName.get("T");
        Iterable<ParameterSpec> parameterSpecs = Arrays.asList(
                ParameterSpec.builder(Names.CLASS_BUNDLE, "source")
                        .addAnnotation(NonNull.class)
                        .build(),
                ParameterSpec.builder(String.class, "key")
                        .addAnnotation(NonNull.class)
                        .build()
        );
        try {
            JavaFile.builder(Names.CLASS_BUNDLER_UTILS.packageName(), TypeSpec.classBuilder(Names.CLASS_BUNDLER_UTILS.simpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(MethodSpec.methodBuilder("getParceler")
                            .addTypeVariable(generic)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addParameters(parameterSpecs)
                            .addParameter(ParameterSpec.builder(generic, "defaultValue")
                                    .addAnnotation(Nullable.class)
                                    .build())
                            .addCode(CodeBlock.of("if (source.containsKey(key))\n" +
                                    "return unwrap(source.getParcelable(key));\n"))
                            .addStatement("return defaultValue")
                            .returns(generic)
                            .build())
                    .addMethod(MethodSpec.methodBuilder("putParceler")
                            .addTypeVariable(generic)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addParameters(parameterSpecs)
                            .addParameter(ParameterSpec.builder(generic, "value")
                                    .addAnnotation(NonNull.class)
                                    .build())
                            .addStatement("source.putParcelable(key, wrap(value))")
                            .build())
                    .build())
                    .addStaticImport(Names.CLASS_PARCELS, "unwrap")
                    .addStaticImport(Names.CLASS_PARCELS, "wrap")
                    .addFileComment("Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler")
                    .build()
                    .writeTo(filer);
        } catch (IOException ignored) {
        }
        return false;
    }

    private boolean isParcelerAvailable() {
        return elementUtils.getTypeElement(Names.CLASS_PARCELS.toString()) != null;
    }
}