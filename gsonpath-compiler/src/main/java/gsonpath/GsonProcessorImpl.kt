package gsonpath

import com.google.common.collect.Sets
import com.squareup.javapoet.ClassName
import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.HandleResult
import gsonpath.generator.factory.TypeAdapterFactoryGenerator
import gsonpath.generator.standard.AutoGsonAdapterGenerator
import gsonpath.model.FieldInfoFactory
import gsonpath.util.*
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

open class GsonProcessorImpl : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>?, env: RoundEnvironment): Boolean {
        if (annotations == null) {
            return false
        }

        val supportedAnnotations = annotations
                .map { ClassName.get(it) }
                .filter {
                    it == ClassName.get(AutoGsonAdapter::class.java) ||
                            it == ClassName.get(AutoGsonAdapterFactory::class.java)
                }

        val customAnnotations: List<TypeElement> = annotations
                .filter { it.getAnnotation(AutoGsonAdapter::class.java) != null }

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return false
        }

        val extensions = loadExtensions() ?: return false

        println()
        printMessage("Started annotation processing")

        val fileWriter = FileWriter(processingEnv)
        val logger = LoggerImpl(processingEnv)
        val typeHandler = ProcessorTypeHandler(processingEnv)
        val defaultValueDetector = DefaultValueDetectorImpl(processingEnv)

        // Handle the standard type adapters.
        val fieldGetterFinder = FieldGetterFinder(typeHandler)
        val adapterGenerator = AutoGsonAdapterGenerator(
                FieldInfoFactory(
                        typeHandler,
                        fieldGetterFinder,
                        AnnotationFetcher(typeHandler, fieldGetterFinder),
                        defaultValueDetector),
                typeHandler, fileWriter, logger)

        val autoGsonAdapterResults: List<HandleResult> =
                getAnnotatedModelElements(env, customAnnotations)
                        .map { (element, autoGsonAdapter) ->
                            printMessage("Generating TypeAdapter ($element)")

                            try {
                                adapterGenerator.handle(element, autoGsonAdapter, ExtensionsHandler(processingEnv, extensions))
                            } catch (e: ProcessingException) {
                                printError(e.message, e.element ?: element)
                                return false
                            }
                        }

        if (autoGsonAdapterResults.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            if (gsonPathFactories.count() == 0) {
                printError("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                        "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                        "See the AutoGsonAdapterFactory annotation for further details.")
                return false
            }

            if (gsonPathFactories.count() > 1) {
                printError("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                return false
            }

            val factoryElement = gsonPathFactories.first()
            try {
                if (!TypeAdapterFactoryGenerator(fileWriter, logger).generate(factoryElement as TypeElement, autoGsonAdapterResults)) {
                    printError("Error while generating TypeAdapterFactory", factoryElement)
                    return false
                }
            } catch (e: ProcessingException) {
                printError(e.message, e.element ?: factoryElement)
                return false
            }
        }

        printMessage("Finished annotation processing")
        println()

        return false
    }

    private fun loadExtensions(): List<GsonPathExtension>? {
        // Load any extensions that are also available at compile time.
        println()
        val extensions: List<GsonPathExtension> =
                try {
                    ServiceLoader.load(GsonPathExtension::class.java, javaClass.classLoader).toList()

                } catch (t: Throwable) {
                    printError("Failed to load one or more GsonPath extensions. Cause: ${t.message}")
                    return null
                }

        // Print the extensions for auditing purposes.
        extensions.forEach {
            printMessage("Extension found: " + it.extensionName)
        }

        return extensions
    }

    private fun printMessage(message: String) {
        println(LOG_PREFIX + message)
    }

    private fun printError(message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message)
    }

    private fun printError(message: String, element: Element) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, LOG_PREFIX + message, element)
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
                .map {
                    ElementAndAutoGson(it as TypeElement, it.getAnnotation(AutoGsonAdapter::class.java))
                }
                .filter {
                    !customAnnotations.contains(it.element)
                }
                .toSet()
                .plus(
                        customAnnotations.flatMap { customAnnotation ->
                            env
                                    .getElementsAnnotatedWith(customAnnotation)
                                    .map {
                                        ElementAndAutoGson(it as TypeElement, customAnnotation.getAnnotation(AutoGsonAdapter::class.java))
                                    }
                        }
                )
    }

    private data class ElementAndAutoGson(
            val element: TypeElement,
            val autoGsonAdapter: AutoGsonAdapter
    )

    companion object {
        private const val LOG_PREFIX = "Gson Path: "
    }

}