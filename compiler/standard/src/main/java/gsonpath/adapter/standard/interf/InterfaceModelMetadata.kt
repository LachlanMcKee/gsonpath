package gsonpath.adapter.standard.interf

import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

data class InterfaceModelMetadata(
        val typeName: TypeName,
        val fieldName: String,
        val enclosedElement: Element,
        val methodName: String,
        val returnTypeMirror: TypeMirror)