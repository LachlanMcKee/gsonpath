package gsonpath.model

import com.squareup.javapoet.TypeName
import gsonpath.util.TypeHandler
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class FieldTypeFactory(private val typeHandler: TypeHandler) {
    fun createFieldType(typeName: TypeName, typeMirror: TypeMirror): FieldType {
        if (typeName.isPrimitive) {
            return FieldType.Primitive(typeName, typeMirror)
        }
        if (typeMirror is ArrayType) {
            return FieldType.MultipleValues.Array(typeName, typeMirror.componentType)
        }
        return attemptCollectionFieldType(typeMirror)
                ?: attemptMapFieldType(typeMirror)
                ?: FieldType.Other(typeName, typeMirror)
    }

    private fun attemptCollectionFieldType(typeMirror: TypeMirror): FieldType.MultipleValues.Collection? {
        val rawType: TypeMirror = when (typeMirror) {
            is DeclaredType -> typeMirror.typeArguments.firstOrNull() ?: return null
            else -> return null
        }

        val collectionType = typeHandler.getDeclaredType(Collection::class, rawType)

        return if (typeHandler.isSubtype(typeMirror, collectionType)) {
            FieldType.MultipleValues.Collection(TypeName.get(typeMirror), rawType)
        } else {
            null
        }
    }

    private fun attemptMapFieldType(typeMirror: TypeMirror): FieldType.MapFieldType? {
        val mapWildcardType = typeHandler.getDeclaredType(Map::class,
                typeHandler.getWildcardType(null, null),
                typeHandler.getWildcardType(null, null))

        return if (typeHandler.isSubtype(typeMirror, mapWildcardType)) {
            FieldType.MapFieldType(TypeName.get(typeMirror), typeMirror)
        } else {
            null
        }
    }
}