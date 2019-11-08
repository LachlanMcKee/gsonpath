package gsonpath.adapter.util

import com.squareup.javapoet.ClassName
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object AdapterFactoryUtil {
    fun <T : Annotation> getAnnotatedModelElements(
            annotationClass: Class<T>,
            env: RoundEnvironment,
            annotations: Set<TypeElement>,
            supportedElementKinds: List<ElementKind>): Set<ElementAndAnnotation<T>> {

        val supportedAnnotations = getSupportedAnnotations(annotations, annotationClass)
        val customAnnotations = getCustomAnnotations(annotations, annotationClass)

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return emptySet()
        }

        return env
                .getElementsAnnotatedWith(annotationClass)
                .asSequence()
                .filter { supportedElementKinds.contains(it.kind) }
                .map {
                    ElementAndAnnotation(it as TypeElement, it.getAnnotation(annotationClass))
                }
                .filter {
                    !customAnnotations.contains(it.element)
                }
                .plus(
                        customAnnotations.flatMap { customAnnotation ->
                            env
                                    .getElementsAnnotatedWith(customAnnotation)
                                    .filter { supportedElementKinds.contains(it.kind) }
                                    .map {
                                        ElementAndAnnotation(it as TypeElement, customAnnotation.getAnnotation(annotationClass))
                                    }
                        }
                )
                .toSet()
    }

    fun getSupportedAnnotations(annotations: Set<TypeElement>, annotationClassName: Class<out Annotation>) =
            annotations
                    .asSequence()
                    .map(ClassName::get)
                    .filter { it == ClassName.get(annotationClassName) }
                    .toList()

    fun getCustomAnnotations(annotations: Set<TypeElement>, annotationClassName: Class<out Annotation>) =
            annotations.filter { it.getAnnotation(annotationClassName) != null }
}

data class ElementAndAnnotation<T : Annotation>(
        val element: TypeElement,
        val annotation: T
)