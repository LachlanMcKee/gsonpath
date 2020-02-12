package gsonpath.adapter

import com.squareup.javapoet.AnnotationSpec
import gsonpath.annotation.GsonPathGenerated

object Constants {
    const val GSON = "gson"
    const val NULL = "null"
    const val IN = "in"
    const val OUT = "out"
    const val VALUE = "value"
    const val GSON_ERRORS = "gsonErrors"
    const val GET_ADAPTER = "$GSON.getAdapter"

    val GENERATED_ANNOTATION: AnnotationSpec = AnnotationSpec.builder(GsonPathGenerated::class.java).build()
}