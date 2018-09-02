package gsonpath.util

import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.Modifier

object MethodSpecExt {
    fun interfaceMethodBuilder(name: String): MethodSpec.Builder {
        return MethodSpec.methodBuilder(name)
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
    }
}