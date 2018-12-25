package gsonpath.generator.adapter

import gsonpath.ProcessingException
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

object SharedFunctions {
    fun getMirroredClass(element: Element, accessorFunc: () -> Unit): TypeMirror {
        return try {
            accessorFunc()
            throw ProcessingException("Unexpected annotation processing defect while obtaining class.", element)
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }
}