package gsonpath.generator.extension

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element

/**
 * Obtains an annotation mirror from an element with the matching class name.
 *
 * @param element the element being inspected for an annotation mirror.
 * @param packageName the package name of the annotation.
 * @param className the class name of the annotation.
 */
fun getAnnotationMirror(element: Element, packageName: String, className: String): AnnotationMirror? {
    return element.annotationMirrors.find {
        ClassName.get(it.annotationType) == ClassName.get(packageName, className)
    } ?: return null
}

/**
 * Obtains an annotation property from an annotation mirror with the matching property name.
 *
 * @param annotationMirror the annotation mirror being inspected for the annotation property.
 * @param propertyName the name of the property to search for.
 */
fun getAnnotationValue(annotationMirror: AnnotationMirror, propertyName: String): AnnotationValue? {
    return annotationMirror.elementValues.toList().find {
        it.first.simpleName.toString() == propertyName
    }?.second
}

/**
 * Obtains an annotation property value from an annotation mirror with the matching property name.
 *
 * @param annotationMirror the annotation mirror being inspected for the annotation property value.
 * @param propertyName the name of the property to search for.
 */
fun getAnnotationValueObject(annotationMirror: AnnotationMirror, propertyName: String): Any? {
    return getAnnotationValue(annotationMirror, propertyName)?.value
}

/**
 * Adds a parser exception with the provided exception text.
 *
 * @param exceptionText the text to add to the exception.
 */
fun CodeBlock.Builder.addException(exceptionText: String): CodeBlock.Builder {
    return this.addStatement("""throw new com.google.gson.JsonParseException("$exceptionText")""")
}