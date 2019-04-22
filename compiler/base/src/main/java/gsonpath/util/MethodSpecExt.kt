package gsonpath.util

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.Modifier

object MethodSpecExt {
    fun overrideMethodBuilder(name: String): MethodSpec.Builder {
        return MethodSpec.methodBuilder(name)
                .addAnnotation(Override::class.java)
    }
}

fun MethodSpec.Builder.code(func: CodeBlock.Builder.() -> Unit): MethodSpec.Builder {
    return addCode(CodeBlock.builder().applyAndBuild(func))
}

fun MethodSpec.Builder.applyAndBuild(func: MethodSpec.Builder.() -> Unit): MethodSpec {
    return apply(func).build()
}