package gsonpath.adapter.util

import com.squareup.javapoet.ClassName
import gsonpath.ProcessingException
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
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

        return addModelElements<T>(env, customAnnotations, supportedElementKinds)
                .plus(addCustomModelElements(env, customAnnotations, supportedElementKinds))
                .toSet()
    }

    inline fun <reified T : Annotation> addModelElements(
            environment: RoundEnvironment,
            customAnnotations: List<TypeElement>,
            supportedElementKinds: List<ElementKind>
    ): Sequence<ElementAndAnnotation<T>> {

        return environment
                .getElementsAnnotatedWith(T::class.java)
                .asSequence()
                .filter {
                    filterElementKind(
                            element = it,
                            supportedElementKinds = supportedElementKinds,
                            allowCustomAnnotation = true,
                            annotationReference = T::class.java)
                }
                .map {
                    ElementAndAnnotation(it as TypeElement, it.getAnnotation(T::class.java))
                }
                .filter {
                    !customAnnotations.contains(it.element)
                }
    }

    inline fun <reified T : Annotation> addCustomModelElements(
            environment: RoundEnvironment,
            customAnnotations: List<TypeElement>,
            supportedElementKinds: List<ElementKind>
    ): List<ElementAndAnnotation<T>> {

        return customAnnotations.flatMap { customAnnotation ->
            environment
                    .getElementsAnnotatedWith(customAnnotation)
                    .filter {
                        filterElementKind(
                                element = it,
                                supportedElementKinds = supportedElementKinds,
                                allowCustomAnnotation = false,
                                annotationReference = customAnnotation)
                    }
                    .map {
                        ElementAndAnnotation(it as TypeElement, customAnnotation.getAnnotation(T::class.java))
                    }
        }
    }

    fun filterElementKind(
            element: Element,
            supportedElementKinds: List<ElementKind>,
            allowCustomAnnotation: Boolean,
            annotationReference: Any
    ): Boolean {

        if (allowCustomAnnotation && element.kind == ElementKind.ANNOTATION_TYPE) {
            return false
        }

        if (!supportedElementKinds.contains(element.kind)) {
            throw ProcessingException("$annotationReference can only be used with types: [${supportedElementKinds.joinToString()}]", element)
        }

        return true
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