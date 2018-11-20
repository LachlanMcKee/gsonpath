package gsonpath.util

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.NoType

class AnnotationFetcher(private val typeHandler: TypeHandler, private val fieldGetterFinder: FieldGetterFinder) {

    fun <T : Annotation> getAnnotation(parentElement: TypeElement, fieldElement: Element, annotationClass: Class<T>): T? {
        return fieldElement.getAnnotationEx(annotationClass)
                ?: findMethodAnnotation(parentElement, fieldElement, annotationClass)
    }

    private fun <T : Annotation> findMethodAnnotation(
            parentElement: TypeElement?,
            fieldElement: Element,
            annotationClass: Class<T>): T? {

        return when {
            parentElement != null && parentElement !is NoType -> {
                fieldGetterFinder.findGetter(parentElement, fieldElement)
                        ?.getAnnotation(annotationClass)
                        ?: findParentMethodAnnotation(parentElement, fieldElement, annotationClass)
            }
            else -> null
        }
    }

    /**
     * Find the annotations from the method within superclass or interfaces.
     */
    private fun <T : Annotation> findParentMethodAnnotation(
            parentElement: TypeElement,
            fieldElement: Element,
            annotationClass: Class<T>): T? {

        val parentMirrors =
                if (parentElement.interfaces.size > 0) {
                    listOf(parentElement.superclass).plus(parentElement.interfaces)
                } else {
                    listOf(parentElement.superclass)
                }

        return parentMirrors
                .asSequence()
                .map { mirror ->
                    findMethodAnnotation(typeHandler.asElement(mirror) as? TypeElement, fieldElement, annotationClass)
                }
                .filterNotNull()
                .firstOrNull()
    }

    fun getAnnotationMirrors(parentElement: TypeElement, fieldElement: Element): List<AnnotationMirror> {
        return fieldElement.annotationMirrors
                .plus(getMethodAnnotationMirrors(parentElement, fieldElement))
    }

    private fun getMethodAnnotationMirrors(parentElement: TypeElement?, fieldElement: Element): List<AnnotationMirror> {
        return if (parentElement != null && parentElement !is NoType) {
            val annotationMirrors = fieldGetterFinder.findGetter(parentElement, fieldElement)
                    ?.annotationMirrors ?: emptyList()

            val superElement = typeHandler.asElement(parentElement.superclass)
            annotationMirrors.plus(getMethodAnnotationMirrors(superElement as? TypeElement, fieldElement))
        } else {
            emptyList()
        }
    }
}