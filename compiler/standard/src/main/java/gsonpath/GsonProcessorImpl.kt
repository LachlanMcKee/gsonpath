package gsonpath

import com.google.common.collect.Sets
import com.squareup.javapoet.ClassName
import gsonpath.generator.HandleResult
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
            logger.printError(e.message, e.element)
        }

        return false
    }

    private fun processInternal(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        val supportedAnnotations = getSupportedAnnotations(annotations)
        val customAnnotations = getCustomAnnotations(annotations)

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return
        }

        println()
        logger.printMessage("Started annotation processing")

        val dependencies = DependencyFactory.create(processingEnv)
        val autoGsonAdapterResults = generateGsonAdapters(env, logger, customAnnotations, dependencies)

        generateTypeAdapterFactories(env, dependencies, autoGsonAdapterResults)

        logger.printMessage("Finished annotation processing")
        println()
    }

    private fun getCustomAnnotations(annotations: Set<TypeElement>) =
            annotations.filter { it.getAnnotation(AutoGsonAdapter::class.java) != null }

    private fun getSupportedAnnotations(annotations: Set<TypeElement>) =
            annotations
                    .asSequence()
                    .map(ClassName::get)
                    .filter { annotationClassName ->
                        annotationClassName == ClassName.get(AutoGsonAdapter::class.java) ||
                                annotationClassName == ClassName.get(AutoGsonAdapterFactory::class.java)
                    }
                    .toList()

    private fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            customAnnotations: List<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        return getAnnotatedModelElements(env, customAnnotations)
                .map { (element, autoGsonAdapter) ->
                    logger.printMessage("Generating TypeAdapter ($element)")

                    dependencies.autoGsonAdapterGenerator.handle(element, autoGsonAdapter)
                }
<<<<<<< Updated upstream
=======

        // Print the extensions for auditing purposes.
        extensions.forEach {
            printMessage("Extension found: " + it.extensionName)
        }

        return extensions.plus(arrayOf(
                IntDefExtension(),
                StringDefExtension(),
                EmptyToNullExtension(),
                FlattenJsonExtension(),
                RemoveInvalidElementsExtension(),
                FloatRangeExtension(),
                IntRangeExtension(),
                SizeExtension(),
                GsonSubTypeExtension(typeHandler, SubTypeMetadataFactoryImpl(typeHandler))
        ))
>>>>>>> Stashed changes
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