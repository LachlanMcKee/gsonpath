package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.adapter.util.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.annotation.AutoGsonAdapter
import gsonpath.annotation.AutoGsonAdapterFactory
import gsonpath.annotation.EnumGsonAdapter
import gsonpath.annotation.GsonSubtype
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

open class GsonProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }

        val logger = Logger(processingEnv)

        try {
            processInternal(annotations, env, logger)
        } catch (e: ProcessingException) {
            logger.printError(e.message, e.element)
        }

        return true
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        println()
        logger.printMessage("Started annotation processing")

        val dependencies = DependencyFactory.create(processingEnv)
        val lazyFactoryMetadata = getTypeAdapterFactoryElement(env, annotations)

        val autoGsonAdapterResults = StandardAdapterFactory
                .generateGsonAdapters(env, logger, lazyFactoryMetadata, annotations, dependencies)
                .plus(SubTypeAdapterFactory.generateGsonAdapters(
                        env, logger, lazyFactoryMetadata, annotations, dependencies))
                .plus(EnumAdapterFactory.generateGsonAdapters(
                        env, logger, lazyFactoryMetadata, annotations, dependencies))

        if (autoGsonAdapterResults.isNotEmpty()) {
            dependencies.typeAdapterFactoryGenerator.generate(
                    lazyFactoryMetadata.element, autoGsonAdapterResults)
        }

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun getTypeAdapterFactoryElement(
            env: RoundEnvironment,
            annotations: Set<TypeElement>
    ): LazyFactoryMetadata {

        val gsonPathFactories = getAnnotatedModelElements<AutoGsonAdapterFactory>(
                env, annotations, listOf(ElementKind.INTERFACE))

        return when {
            gsonPathFactories.count() > 1 -> {
                throw ProcessingException("Only one interface annotated with @AutoGsonAdapterFactory can exist")
            }
            else -> LazyFactoryMetadata(gsonPathFactories.firstOrNull())
        }
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return incrementalMetadata
                ?.let { (customAnnotations) ->
                    customAnnotations.plus(setOf(
                            AutoGsonAdapterFactory::class.java.canonicalName,
                            AutoGsonAdapter::class.java.canonicalName,
                            EnumGsonAdapter::class.java.canonicalName,
                            GsonSubtype::class.java.canonicalName
                    ))
                }
                ?: setOf("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): Set<String> {
        return if (incrementalMetadata != null) {
            setOf("org.gradle.annotation.processing.aggregating")
        } else {
            emptySet()
        }
    }

    private val incrementalMetadata: IncrementalMetadata? by lazy {
        if ("true" != processingEnv.options[OPTION_INCREMENTAL]) {
            null
        } else {
            processingEnv.options[OPTION_ADDITIONAL_ANNOTATIONS]
                    ?.let { IncrementalMetadata(it.split(",").toSet()) }
                    ?: IncrementalMetadata(emptySet())
        }
    }

    private data class IncrementalMetadata(val customAnnotations: Set<String>)

    private companion object {
        private const val OPTION_INCREMENTAL = "gsonpath.incremental"
        private const val OPTION_ADDITIONAL_ANNOTATIONS = "gsonpath.additionalAnnotations"
    }
}