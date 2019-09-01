package gsonpath.adapter.common

import com.squareup.javapoet.TypeName

data class SubTypeMetadata(
        val gsonSubTypeFieldInfo: List<GsonSubTypeFieldInfo>,
        val classGetterMethodName: String)

data class GsonSubTypeFieldInfo(
        val jsonKey: String,
        val variableName: String,
        val parameterTypeName: TypeName,
        val nullable: Boolean)
