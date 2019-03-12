package gsonpath

import com.squareup.javapoet.ClassName
import gsonpath.generator.HandleResult
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

object StandardAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        val supportedAnnotations = getSupportedAnnotations(annotations)
        val customAnnotations = getCustomAnnotations(annotations)

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return emptyList()
        }

        return getAnnotatedModelElements(env, customAnnotations)
                .map { (element, autoGsonAdapter) ->
                    logger.printMessage("Generating TypeAdapter ($element)")

                    dependencies.autoGsonAdapterGenerator.handle(element, autoGsonAdapter)
                }
    }

    private fun getSupportedAnnotations(annotations: Set<TypeElement>) =
            annotations
                    .asSequence()
                    .map(ClassName::get)
                    .filter { annotationClassName ->
                        annotationClassName == ClassName.get(AutoGsonAdapter::class.java) ||
                                annotationClassName == ClassName.get(AutoGsonAdapterFactory::class.java)
                    }
                    .toList()

    private fun getCustomAnnotations(annotations: Set<TypeElement>) =
            annotations.filter { it.getAnnotation(AutoGsonAdapter::class.java) != null }

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