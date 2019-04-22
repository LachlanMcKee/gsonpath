package gsonpath.adapter

import com.squareup.javapoet.AnnotationSpec
import javax.annotation.Generated

object Constants {
    const val GSON = "gson"
    const val NULL = "null"
    const val IN = "in"
    const val OUT = "out"
    const val CONTINUE = "continue"
    const val BREAK = "break"
    const val VALUE = "value"
    const val GET_ADAPTER = "$GSON.getAdapter"

    val GENERATED_ANNOTATION: AnnotationSpec = AnnotationSpec.builder(Generated::class.java).run {
        addMember("value", "\"gsonpath.GsonProcessor\"")
        addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
        build()
    }
}