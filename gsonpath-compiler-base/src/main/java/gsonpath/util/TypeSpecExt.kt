package gsonpath.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

object TypeSpecExt {
    fun finalClassBuilder(name: String): TypeSpec.Builder {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }

    fun finalClassBuilder(className: ClassName): TypeSpec.Builder {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }
}

fun TypeSpec.Builder.method(name: String, func: MethodSpec.Builder.() -> Unit) {
    addMethod(MethodSpec.methodBuilder(name).applyAndBuild(func))
}

fun TypeSpec.Builder.interfaceMethod(name: String, func: MethodSpec.Builder.() -> Unit) {
    addMethod(MethodSpecExt.interfaceMethodBuilder(name).applyAndBuild(func))
}

fun TypeSpec.Builder.constructor(func: MethodSpec.Builder.() -> Unit) {
    addMethod(MethodSpec.constructorBuilder().applyAndBuild(func))
}