package gsonpath.model

import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException

class GsonObjectValidator {

    @Throws(ProcessingException::class)
    fun validate(fieldInfo: FieldInfo): Result {
        val fieldTypeName = fieldInfo.fieldType.typeName

        if (fieldTypeName == TypeName.OBJECT) {
            throw ProcessingException("Invalid field type: $fieldTypeName", fieldInfo.element)
        }

        // Attempt to find a Nullable or NonNull annotation type.
        val isOptional: Boolean = fieldInfo.annotationNames.any { it == "Nullable" }
        val isMandatory: Boolean = fieldInfo.annotationNames.any {
            arrayOf("NonNull", "Nonnull", "NotNull", "Notnull").contains(it)
        }

        // Fields cannot use both annotations.
        if (isMandatory && isOptional) {
            throw ProcessingException("Field cannot have both Mandatory and Optional annotations", fieldInfo.element)
        }

        // Primitives should not use either annotation.
        if ((fieldInfo.fieldType is FieldType.Primitive) && (isMandatory || isOptional)) {
            throw ProcessingException("Primitives should not use NonNull or Nullable annotations", fieldInfo.element)
        }

        return when {
            isMandatory -> Result.Mandatory
            isOptional -> Result.Optional
            else -> Result.Standard
        }
    }

    sealed class Result {
        object Mandatory : Result()
        object Optional : Result()
        object Standard : Result()
    }
}