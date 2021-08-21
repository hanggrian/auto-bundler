package com.hendraanggrian.auto.bundles.compiler

import com.google.auto.common.MoreElements
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Sets
import com.hendraanggrian.auto.bundles.BindExtra
import com.hendraanggrian.auto.bundles.BindState
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class BundlesProcessor : AbstractProcessor() {

    companion object {
        private val SUPPORTED_ANNOTATIONS = setOf(BindExtra::class.java, BindState::class.java)
    }

    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> = SUPPORTED_ANNOTATIONS.map { it.canonicalName }.toSet()

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
    }

    @Suppress("UnstableApiUsage")
    override fun process(set: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // build utility class if parceler is available and if it has not yet already been created
        if (elementUtils.getTypeElement("$PARCELS") != null &&
            elementUtils.getTypeElement("$BUNDLES_UTILS") == null
        ) {
            filer.writeBundlesUtils()
        }
        // preparing elements
        for (annotation in SUPPORTED_ANNOTATIONS) {
            val map = LinkedHashMultimap.create<TypeElement, Element>()
            val generatedClassNames = Sets.newHashSet<String>()
            for (fieldElement in roundEnv.getElementsAnnotatedWith(annotation)) {
                val typeElement = MoreElements.asType(fieldElement.enclosingElement)
                map.put(typeElement, fieldElement)
                generatedClassNames.add(
                    typeElement.getMeasuredName(
                        when (annotation) {
                            BindExtra::class.java -> BindExtra.SUFFIX
                            else -> BindState.SUFFIX
                        }
                    )
                )
            }
            // write classes and keep results
            map.keySet().forEach {
                when (annotation) {
                    BindExtra::class.java -> filer.writeExtraBinding(it, generatedClassNames, map[it], typeUtils)
                    BindState::class.java -> filer.writeStateBinding(it, generatedClassNames, map[it], typeUtils)
                }
            }
        }
        return false
    }
}
