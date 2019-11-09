package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

open class GsonPathAdapterProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null || annotations.isEmpty()) {
            return false
        }

        val logger = Logger(processingEnv)

        try {
            processInternal(annotations, env, logger)
        } catch (e: ProcessingException) {
            logger.printError(e.message, e.element)
        }

        return false
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        println()
        logger.printMessage("Started annotation processing")

        Thread.sleep(5000)

        val dependencies = DependencyFactory.create(processingEnv)
        StandardAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
        SubTypeAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
        EnumAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)

        logger.printMessage("Finished annotation processing")
        println()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val additonalAnnotations: Set<String> = processingEnv.options["gsonpath.addtionalAnnotations"]
                ?.split(",")
                ?.toSet()
                ?: emptySet()

        println("GsonPathAdapterProcessor. additonalAnnotations: $additonalAnnotations")

        return additonalAnnotations.plus(setOf(
                AutoGsonAdapter::class.java.canonicalName,
                GsonSubtype::class.java.canonicalName
        ))
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}