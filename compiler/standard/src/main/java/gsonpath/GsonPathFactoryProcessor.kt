package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.Dependencies
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

open class GsonPathFactoryProcessor : AbstractProcessor() {

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

        val dependencies = DependencyFactory.create(processingEnv)

        generateTypeAdapterFactories(env, annotations, dependencies)

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun generateTypeAdapterFactories(
            env: RoundEnvironment,
            annotations: Set<TypeElement>,
            dependencies: Dependencies) {

        val typeAdapterElements =
                StandardAdapterFactory.getHandledElements(env, annotations)
                        .plus(SubTypeAdapterFactory.getHandledElements(env, annotations))
                        .plus(EnumAdapterFactory.getHandledElements(env, annotations))

        if (typeAdapterElements.isNotEmpty()) {
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
                    dependencies.typeAdapterFactoryGenerator.generate(factoryElement as TypeElement, typeAdapterElements)
                }
            }
        }
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val additonalAnnotations: Set<String> = processingEnv.options["gsonpath.addtionalAnnotations"]
                ?.split(",")
                ?.toSet()
                ?: emptySet()

        println("GsonPathAdapterProcessor. additonalAnnotations: $additonalAnnotations")

        return additonalAnnotations.plus(setOf(
                AutoGsonAdapterFactory::class.java.canonicalName,

                // Used to find the classes generated via GsonPathAdapterProcessor
                AutoGsonAdapter::class.java.canonicalName,
                GsonSubtype::class.java.canonicalName
        ))
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}