package gsonpath

import com.google.common.collect.Sets
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.Dependencies
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

open class GsonProcessorImpl : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }

        val startTime = System.currentTimeMillis()

        val logger = Logger(processingEnv)

        try {
            processInternal(annotations, env, logger)
        } catch (e: ProcessingException) {
            logger.printError(e.message, e.element)
        }

        logger.printMessage("Duration: ${System.currentTimeMillis() - startTime}")

        return false
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        println()
        logger.printMessage("Started annotation processing")

        val dependencies = DependencyFactory.create(processingEnv)
        val autoGsonAdapterResults = StandardAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
                .plus(SubTypeAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies))

        generateTypeAdapterFactories(env, dependencies, autoGsonAdapterResults)

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun generateTypeAdapterFactories(
            env: RoundEnvironment,
            dependencies: Dependencies,
            autoGsonAdapterResults: List<AdapterGenerationResult>) {

        if (autoGsonAdapterResults.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            when {
                gsonPathFactories.count() == 0 -> {
                    throw ProcessingException("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                            "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                            "See the AutoGsonAdapterFactory annotation for further details.")
                }
                gsonPathFactories.count() > 1 -> {
                    throw ProcessingException("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                }
                else -> {
                    val factoryElement = gsonPathFactories.first()
                    dependencies.typeAdapterFactoryGenerator.generate(factoryElement as TypeElement, autoGsonAdapterResults)
                }
            }
        }
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return Sets.newHashSet("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}