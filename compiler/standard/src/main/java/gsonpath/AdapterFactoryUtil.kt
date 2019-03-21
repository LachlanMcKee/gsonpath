package gsonpath

import com.squareup.javapoet.ClassName
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object AdapterFactoryUtil {
    inline fun <reified T : Annotation> getAnnotatedModelElements(
            env: RoundEnvironment,
            annotations: Set<TypeElement>,
            supportedElementKinds: List<ElementKind> = listOf(ElementKind.CLASS)): Set<ElementAndAnnotation<T>> {

        val supportedAnnotations = getSupportedAnnotations(annotations, T::class.java)
        val customAnnotations = getCustomAnnotations(annotations, T::class.java)

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return emptySet()
        }

        return env
                .getElementsAnnotatedWith(T::class.java)
                .asSequence()
                .filter { supportedElementKinds.contains(it.kind) }
                .map {
                    ElementAndAnnotation(it as TypeElement, it.getAnnotation(T::class.java))
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
                                        ElementAndAnnotation(it as TypeElement, customAnnotation.getAnnotation(T::class.java))
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