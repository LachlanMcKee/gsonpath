package gsonpath.generator.adapter

import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

object SharedFunctions {
    fun getMirroredClass(fieldInfo: FieldInfo, accessorFunc: () -> Unit): TypeMirror {
        return try {
            accessorFunc()
            throw ProcessingException("Unexpected annotation processing defect while obtaining class.",
                    fieldInfo.element)
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }
}