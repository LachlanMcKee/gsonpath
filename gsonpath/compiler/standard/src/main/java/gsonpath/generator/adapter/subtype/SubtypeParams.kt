package gsonpath.generator.adapter.subtype

import gsonpath.model.GsonField
import gsonpath.model.SubTypeMetadata

data class SubtypeParams(
        val subTypedFields: List<SubTypedField>
)

data class SubTypedField(
        val subTypeMetadata: SubTypeMetadata,
        val gsonField: GsonField,
        val isFieldArrayType: Boolean
)