package gsonpath.generator.standard

import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.model.FieldInfo
import gsonpath.model.GsonField
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

object SharedFunctions {
    fun validateFieldAnnotations(fieldInfo: FieldInfo) {
        // For now, we only ensure that the flatten annotation is only added to a String.
        if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
            return
        }

        if (fieldInfo.typeName != CLASS_NAME_STRING) {
            throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
        }
    }

    fun getMirroredClass(gsonField: GsonField, accessorFunc: () -> Unit): TypeMirror {
        return try {
            accessorFunc()
            throw ProcessingException("Unexpected annotation processing defect while obtaining class.",
                    gsonField.fieldInfo.element)
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }
}