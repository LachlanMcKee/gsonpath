package gsonpath.util

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.NoType

class AnnotationFetcher(private val typeHandler: TypeHandler, private val fieldGetterFinder: FieldGetterFinder) {

    fun <T : Annotation> getAnnotation(parentElement: TypeElement, fieldElement: Element, annotationClass: Class<T>): T? {
        val memberAnnotation = fieldElement.getAnnotation(annotationClass)
        if (memberAnnotation != null) {
            return memberAnnotation
        }
        return findMethodAnnotation(parentElement, fieldElement, annotationClass)
    }

    private fun <T : Annotation> findMethodAnnotation(
            modelElement: TypeElement?,
            memberElement: Element,
            annotationClass: Class<T>): T? {

        if (modelElement != null && modelElement !is NoType) {
            val annotation = fieldGetterFinder.findGetter(modelElement, memberElement)
                    ?.getAnnotation(annotationClass)

            if (annotation != null) {
                return annotation
            }

            return findMethodAnnotation(typeHandler.asElement(modelElement.superclass) as? TypeElement,
                    memberElement, annotationClass)
        }
        return null
    }

    fun getAnnotationMirrors(parentElement: TypeElement, fieldElement: Element): List<AnnotationMirror> {
        return fieldElement.annotationMirrors
                .plus(getMethodAnnotationMirrors(parentElement, fieldElement))
    }

    private fun getMethodAnnotationMirrors(modelElement: TypeElement?, memberElement: Element): List<AnnotationMirror> {
        return if (modelElement != null && modelElement !is NoType) {
            val annotationMirrors = fieldGetterFinder.findGetter(modelElement, memberElement)
                    ?.annotationMirrors ?: emptyList()

            val superElement = typeHandler.asElement(modelElement.superclass)
            annotationMirrors.plus(getMethodAnnotationMirrors(superElement as? TypeElement, memberElement))
        } else {
            emptyList()
        }
    }
}