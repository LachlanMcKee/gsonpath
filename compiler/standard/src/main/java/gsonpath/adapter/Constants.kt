package gsonpath.adapter

import com.squareup.javapoet.AnnotationSpec
import gsonpath.GsonPathGenerated

object Constants {
    const val LISTENER = "listener"
    const val GSON = "gson"
    const val NULL = "null"
    const val IN = "in"
    const val OUT = "out"
    const val VALUE = "value"
    const val GET_ADAPTER = "$GSON.getAdapter"

    val GENERATED_ANNOTATION: AnnotationSpec = AnnotationSpec.builder(GsonPathGenerated::class.java).build()
}