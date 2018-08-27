package gsonpath.util

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.NoType

class AnnotationFetcher(private val typeHandler: TypeHandler, private val fieldGetterFinder: FieldGetterFinder) {

    fun <T : Annotation> getAnnotation(parentElement: TypeElement, fieldElement: Element, annotationClass: Class<T>): T? {
        return fieldElement.getAnnotation(annotationClass)
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
                        ?: findMethodAnnotation(typeHandler.asElement(parentElement.superclass) as? TypeElement,
                                fieldElement, annotationClass)
            }
            else -> null
        }
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