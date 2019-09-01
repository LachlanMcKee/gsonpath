package gsonpath.adapter.common

import com.squareup.javapoet.MethodSpec

data class GsonSubTypeResult(
        val readMethodSpecs: MethodSpec,
        val writeMethodSpecs: MethodSpec
)