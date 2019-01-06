package gsonpath.model

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

sealed class FieldType {
    abstract val typeName: TypeName

    data class Primitive(
            override val typeName: TypeName) : FieldType()

    data class Other(
            override val typeName: TypeName) : FieldType()

    data class MapFieldType(
            override val typeName: TypeName) : FieldType()

    sealed class MultipleValues : FieldType() {
        abstract val elementTypeMirror: TypeMirror

        data class Array(
                override val typeName: TypeName,
                override val elementTypeMirror: TypeMirror) : MultipleValues()

        data class Collection(
                override val typeName: TypeName,
                override val elementTypeMirror: TypeMirror) : MultipleValues()
    }
}