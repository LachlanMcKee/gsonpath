package gsonpath.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName

object TypeNameExt {
    fun createMap(keyTypeName: TypeName, valueTypeName: TypeName): TypeName {
        return ParameterizedTypeName.get(ClassName.get(Map::class.java), keyTypeName, valueTypeName)
    }
}