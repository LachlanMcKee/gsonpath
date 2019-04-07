package gsonpath.adapter.common

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

data class GsonSubTypeResult(
        val constructorCodeBlock: CodeBlock? = null,
        val fieldSpecs: List<FieldSpec> = emptyList(),
        val typeSpecs: List<TypeSpec> = emptyList(),
        val readMethodSpecs: MethodSpec,
        val writeMethodSpecs: MethodSpec
)