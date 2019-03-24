package gsonpath.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName

object TypeNameExt {
    private fun createMapType(mapType: ClassName, keyTypeName: TypeName, valueTypeName: TypeName): TypeName {
        return ParameterizedTypeName.get(mapType, keyTypeName, valueTypeName)
    }

    fun createMap(keyTypeName: TypeName, valueTypeName: TypeName): TypeName {
        return createMapType(ClassName.get(Map::class.java), keyTypeName, valueTypeName)
    }

    fun createHashMap(keyTypeName: TypeName, valueTypeName: TypeName): TypeName {
        return createMapType(ClassName.get(HashMap::class.java), keyTypeName, valueTypeName)
    }
}