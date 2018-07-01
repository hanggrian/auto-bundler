package com.hendraanggrian.bundler.compiler

import com.google.auto.common.MoreElements
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Sets
import com.hendraanggrian.bundler.Extra
import com.hendraanggrian.bundler.State
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class BundlerProcessor : AbstractProcessor() {

    companion object {
        private val SUPPORTED_ANNOTATIONS = setOf(Extra::class.java, State::class.java)
    }

    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> = SUPPORTED_ANNOTATIONS
        .map { it.canonicalName }.toSet()

    @Synchronized override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
    }

    override fun process(set: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // build utility class if parceler is available and if it has not yet already been created
        if (elementUtils.getTypeElement(TYPE_PARCELS.toString()) != null &&
            elementUtils.getTypeElement(TYPE_BUNDLER_UTILS.toString()) == null) {
            val file = UtilsSpec().toJavaFile()
            try {
                file.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
        // preparing elements
        for (annotation in SUPPORTED_ANNOTATIONS) {
            val map = LinkedHashMultimap.create<TypeElement, Element>()
            val generatedClassNames = Sets.newHashSet<String>()
            for (fieldElement in roundEnv.getElementsAnnotatedWith(annotation)) {
                val typeElement = MoreElements.asType(fieldElement.enclosingElement)
                map.put(typeElement, fieldElement)
                generatedClassNames.add(typeElement.getMeasuredName(when (annotation) {
                    Extra::class.java -> Extra.SUFFIX
                    else -> State.SUFFIX
                }))
            }
            // write classes and keep results
            map.keySet()
                .map {
                    (if (annotation == Extra::class.java) ExtraBindingSpec(it)
                    else StateBindingSpec(it))
                        .superclass(generatedClassNames)
                        .statement(map.get(it), typeUtils)
                        .toJavaFile()
                }
                .forEach {
                    try {
                        it.writeTo(filer)
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
        }
        return false
    }
}