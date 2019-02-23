package gsonpath.generator.extension.subtype

import gsonpath.model.FieldType

sealed class GsonSubTypeCategory {
    abstract val fieldType: FieldType

    data class SingleValue(override val fieldType: FieldType.Other) : GsonSubTypeCategory()
    data class MultipleValues(override val fieldType: FieldType.MultipleValues) : GsonSubTypeCategory()
}