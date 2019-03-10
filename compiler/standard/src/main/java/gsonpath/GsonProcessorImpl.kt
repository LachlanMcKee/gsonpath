package gsonpath

import com.google.common.collect.Sets
import com.squareup.javapoet.ClassName
import gsonpath.generator.HandleResult
import gsonpath.util.ProcessorTypeHandler
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

open class GsonProcessorImpl : AbstractProcessor() {

    private val typeHandler = ProcessorTypeHandler(processingEnv)
    private val logger = Logger(processingEnv)

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }

        val supportedAnnotations = annotations
                .asSequence()
                .map { ClassName.get(it) }
                .filter {
                    it == ClassName.get(AutoGsonAdapter::class.java) ||
                            it == ClassName.get(AutoGsonAdapterFactory::class.java)
                }
                .toList()

        val customAnnotations: List<TypeElement> = annotations
                .filter { it.getAnnotation(AutoGsonAdapter::class.java) != null }

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return false
        }

        val extensions = ExtensionsLoader.loadExtensions(typeHandler, logger) ?: return false
        val (adapterGenerator, typeAdapterFactoryGenerator) = DependencyFactory.create(processingEnv, extensions)

        println()
        logger.printMessage("Started annotation processing")

        val autoGsonAdapterResults: List<HandleResult> =
                getAnnotatedModelElements(env, customAnnotations)
                        .map { (element, autoGsonAdapter) ->
                            logger.printMessage("Generating TypeAdapter ($element)")

                            try {
                                adapterGenerator.handle(element, autoGsonAdapter)
                            } catch (e: ProcessingException) {
                                logger.printError(e.message, e.element ?: element)
                                return false
                            }
                        }

        if (autoGsonAdapterResults.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            if (gsonPathFactories.count() == 0) {
                logger.printError("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                        "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                        "See the AutoGsonAdapterFactory annotation for further details.")
                return false
            }

            if (gsonPathFactories.count() > 1) {
                logger.printError("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                return false
            }

            val factoryElement = gsonPathFactories.first()
            try {
                if (!typeAdapterFactoryGenerator.generate(factoryElement as TypeElement, autoGsonAdapterResults)) {
                    logger.printError("Error while generating TypeAdapterFactory", factoryElement)
                    return false
                }
            } catch (e: ProcessingException) {
                logger.printError(e.message, e.element ?: factoryElement)
                return false
            }
        }

        logger.printMessage("Finished annotation processing")
        println()

        return false
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return Sets.newHashSet("*")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun getAnnotatedModelElements(env: RoundEnvironment,
                                          customAnnotations: List<TypeElement>): Set<ElementAndAutoGson> {
        return env
                .getElementsAnnotatedWith(AutoGsonAdapter::class.java)
                .asSequence()
                .map {
                    ElementAndAutoGson(it as TypeElement, it.getAnnotation(AutoGsonAdapter::class.java))
                }
                .filter {
                    !customAnnotations.contains(it.element)
                }
                .plus(
                        customAnnotations.flatMap { customAnnotation ->
                            env
                                    .getElementsAnnotatedWith(customAnnotation)
                                    .map {
                                        ElementAndAutoGson(it as TypeElement, customAnnotation.getAnnotation(AutoGsonAdapter::class.java))
                                    }
                        }
                )
                .toSet()
    }

    private data class ElementAndAutoGson(
            val element: TypeElement,
            val autoGsonAdapter: AutoGsonAdapter
    )

}