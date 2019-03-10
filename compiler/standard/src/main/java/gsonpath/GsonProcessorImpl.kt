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

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }


        val logger = Logger(processingEnv)

        try {
            processInternal(annotations, env, logger)
        } catch (e: ProcessingException) {
            e.element.let { element ->
                if (element != null) {
                    logger.printError(e.message, element)
                } else {
                    logger.printError(e.message)
                }
            }
        }

        return false
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        val supportedAnnotations = annotations
                .asSequence()
                .map(ClassName::get)
                .filter { annotationClassName ->
                    annotationClassName == ClassName.get(AutoGsonAdapter::class.java) ||
                            annotationClassName == ClassName.get(AutoGsonAdapterFactory::class.java)
                }
                .toList()

        val customAnnotations: List<TypeElement> = annotations
                .filter { it.getAnnotation(AutoGsonAdapter::class.java) != null }

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return
        }

        val typeHandler = ProcessorTypeHandler(processingEnv)
        val extensions = ExtensionsLoader.loadExtensions(typeHandler, logger)
        val dependencies = DependencyFactory.create(processingEnv, extensions)

        println()
        logger.printMessage("Started annotation processing")

        val autoGsonAdapterResults = generateGsonAdapters(env, logger, customAnnotations, dependencies)

        generateTypeAdapterFactories(env, dependencies, autoGsonAdapterResults)

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            customAnnotations: List<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        return getAnnotatedModelElements(env, customAnnotations)
                .map { (element, autoGsonAdapter) ->
                    logger.printMessage("Generating TypeAdapter ($element)")

                    try {
                        dependencies.autoGsonAdapterGenerator.handle(element, autoGsonAdapter)
                    } catch (e: ProcessingException) {
                        logger.printError(e.message, e.element ?: element)
                        throw e
                    }
                }
    }

    private fun generateTypeAdapterFactories(
            env: RoundEnvironment,
            dependencies: Dependencies,
            autoGsonAdapterResults: List<HandleResult>) {

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