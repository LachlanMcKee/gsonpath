package gsonpath.model

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

sealed class FieldType {
    abstract val typeName: TypeName
    abstract val elementTypeMirror: TypeMirror

    data class Primitive(
            override val typeName: TypeName,
            override val elementTypeMirror: TypeMirror) : FieldType()

    data class Other(
            override val typeName: TypeName,
            override val elementTypeMirror: TypeMirror) : FieldType()

    data class MapFieldType(
            override val typeName: TypeName,
            override val elementTypeMirror: TypeMirror) : FieldType()

    sealed class MultipleValues : FieldType() {

        data class Array(
                override val typeName: TypeName,
                override val elementTypeMirror: TypeMirror) : MultipleValues()

        data class Collection(
                override val typeName: TypeName,
                override val elementTypeMirror: TypeMirror) : MultipleValues()
    }
}