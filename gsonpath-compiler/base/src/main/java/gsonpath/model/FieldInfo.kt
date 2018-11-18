package gsonpath.model

import com.squareup.javapoet.TypeName

import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

/**
 * Contains important information about a class field within a class annotated with the AutoGsonAdapter annotation.
 */
interface FieldInfo {
    val typeName: TypeName

    val typeMirror: TypeMirror

    /**
     * The name of the class that field is contained within.
     */
    val parentClassName: String

    /**
     * Searches for an annotation that is attached to the field.
     */
    fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

    /**
     * The name of the field this [FieldInfo] represents.
     */
    val fieldName: String

    /**
     * The mechanism for accessing the field from the class.
     *
     * This allows the library to either access the variable via an exposed field, or potentially a getter instead.
     */
    val fieldAccessor: String

    /**
     * Returns the raw names of all annotations attached to the field.
     *
     * This is useful for finding annotations without having the annotation class included in the
     * annotation processor library.
     */
    val annotationNames: List<String>

    /**
     * The raw annotation processor field element.
     * This should mainly be used for reporting errors back to the user.
     */
    val element: Element

    /**
     * Whether the field has a default value assigned.
     */
    val hasDefaultValue: Boolean
}
