package gsonpath.generator.adapter

import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.model.FieldInfo

object AdapterAnnotationValidator {
    fun validateFieldAnnotations(fieldInfo: FieldInfo) {
        // For now, we only ensure that the flatten annotation is only added to a String.
        if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
            return
        }

        if (fieldInfo.typeName != CLASS_NAME_STRING) {
            throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
        }
    }
}