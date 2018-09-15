package gsonpath.util

import com.squareup.javapoet.FieldSpec

fun FieldSpec.Builder.applyAndBuild(func: FieldSpec.Builder.() -> Unit): FieldSpec {
    return apply(func).build()
}
