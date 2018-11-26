package gsonpath.extension.def

import gsonpath.extension.getAnnotationMirror
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element

/**
 * The data structure that represents the 'StringDef/IntDef' annotation and the annotation that is annotated by it.
 */
data class DefAnnotationMirrors(val annotationMirror: AnnotationMirror, val defAnnotationMirror: AnnotationMirror)

/**
 * Obtains the annotation mirrors required for the 'StringDef/IntDef' annotations.
 *
 * These annotations differ compared to the others in the library, since the an intermediate annotation is defined
 * and annotated with the 'StringDef/IntDef' annotations.
 *
 * @param element the element that is potentially annotated
 * @param packageName the package name of the support annotation.
 * @param className the class name of the support annotation.
 */
fun getDefAnnotationMirrors(element: Element, packageName: String, className: String): DefAnnotationMirrors? {

    // Find the field annotation that itself is annotated with an annotation that matches the class name.
    val definitionAnnotation: AnnotationMirror = element.annotationMirrors?.find {
        getAnnotationMirror(it.annotationType.asElement(), packageName, className) != null
    } ?: return null

    // Obtain the actual 'StringDef/IntDef' annotation mirror that annotates the annotation mirror above.
    val defAnnotationMirror = getAnnotationMirror(definitionAnnotation.annotationType.asElement(),
            packageName, className) ?: return null

    return DefAnnotationMirrors(definitionAnnotation, defAnnotationMirror)
}