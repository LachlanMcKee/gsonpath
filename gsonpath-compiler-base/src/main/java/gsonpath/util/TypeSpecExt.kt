package gsonpath.util

import com.squareup.javapoet.ClassName
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