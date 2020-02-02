package gsonpath.adapter.enums

import com.squareup.javapoet.ClassName

data class EnumAdapterProperties(
        val enumTypeName: ClassName,
        val fields: List<EnumField>,
        val defaultValue: EnumField?
) {
    data class EnumField(
            val enumValueTypeName: ClassName,
            val label: String
    )
}