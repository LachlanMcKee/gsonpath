package gsonpath.util

import javax.lang.model.element.Element

/**
 * Fetches the annotation from the element directly.
 *
 * Failing that it attempts to find any annotations that are annotated with the annotation type. The method then returns
 * that annotation. This allows easy annotation reuse.
 */
fun <A : Annotation> Element.getAnnotationEx(annotationType: Class<A>): A? {
    val immediateAnnotation = getAnnotation(annotationType)
    if (immediateAnnotation != null) {
        return immediateAnnotation
    }

    // See if there are any annotations that are themselves annotated by the annotation we want.
    return annotationMirrors
            .asSequence()
            .mapNotNull { it.annotationType.asElement().getAnnotation(annotationType) }
            .firstOrNull()
}