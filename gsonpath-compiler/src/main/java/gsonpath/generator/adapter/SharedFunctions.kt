package gsonpath.generator.adapter

import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.model.FieldInfo
import gsonpath.model.GsonField
import gsonpath.util.TypeHandler
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
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

    fun getMirroredClass(fieldInfo: FieldInfo, accessorFunc: () -> Unit): TypeMirror {
        return try {
            accessorFunc()
            throw ProcessingException("Unexpected annotation processing defect while obtaining class.",
                    fieldInfo.element)
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }


    /**
     * Determines whether the type is an array or a collection type.
     */
    fun isArrayType(typeHandler: TypeHandler, gsonField: GsonField): Boolean {
        val typeMirror = gsonField.fieldInfo.typeMirror
        if (typeMirror is ArrayType) {
            return true
        }

        if (typeHandler.isMirrorOfCollectionType(typeMirror)) {
            return false
        }

        throw ProcessingException("Unexpected type found for GsonSubtype field, ensure you either use " +
                "an array, or a collection class (List, Collection, etc).", gsonField.fieldInfo.element)
    }


    /**
     * Obtains the actual type name that is either contained within the array or the list.
     * e.g. for 'String[]' or 'List<String>' the returned type name is 'String'
     */
    fun getRawType(fieldInfo: FieldInfo): TypeMirror {
        val typeMirror = fieldInfo.typeMirror
        return when (typeMirror) {
            is ArrayType -> typeMirror.componentType

            is DeclaredType -> typeMirror.typeArguments.first()

            else -> throw ProcessingException("Unexpected type found for GsonSubtype field, ensure you either use " +
                    "an array, or a List class.", fieldInfo.element)
        }
    }

    fun getRawType(gsonField: GsonField) = getRawType(gsonField.fieldInfo)
}