package gsonpath.util

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

interface TypeHandler {
    fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean
    fun asElement(t: TypeMirror): Element?
}

class ProcessorTypeHandler(private val processingEnv: ProcessingEnvironment) : TypeHandler {
    override fun asElement(t: TypeMirror): Element? {
        return processingEnv.typeUtils.asElement(t)
    }

    override fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean {
        return processingEnv.typeUtils.isSubtype(t1, t2)
    }
}