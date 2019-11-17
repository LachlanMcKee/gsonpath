package gsonpath.util

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import gsonpath.GeneratedAdapter

object GeneratedAdapterUtil {
    fun createGeneratedAdapterAnnotation(vararg classNames: ClassName): AnnotationSpec? {
        val classNamesString = classNames.joinToString { "\"$it\"" }
        return AnnotationSpec.builder(GeneratedAdapter::class.java)
                .addMember("adapterElementClassNames", "{$classNamesString}")
                .build()
    }
}