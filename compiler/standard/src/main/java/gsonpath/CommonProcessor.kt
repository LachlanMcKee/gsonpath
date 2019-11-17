package gsonpath

import com.google.common.collect.Sets
import gsonpath.dependencies.Dependencies
import gsonpath.dependencies.DependencyFactory
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

abstract class CommonProcessor : AbstractProcessor() {

    abstract fun handleAnnotations(annotations: Set<TypeElement>, env: RoundEnvironment, dependencies: Dependencies)

    abstract fun processorDescription(): String

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null || annotations.isEmpty()) {
            return false
        }

        val dependencies = DependencyFactory.create(processingEnv)
        val logger = dependencies.logger

        try {
            val description = processorDescription()
            logger.printMessage("Started processing $description")
            handleAnnotations(annotations, env, dependencies)
            logger.printMessage("Finished processing $description")

        } catch (e: ProcessingException) {
            logger.printError(e.message, e.element)
        }

        return false
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return Sets.newHashSet("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}