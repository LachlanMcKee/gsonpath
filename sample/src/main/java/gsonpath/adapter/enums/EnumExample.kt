package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import gsonpath.annotation.AutoGsonAdapter
import gsonpath.annotation.EnumGsonAdapter

@AutoGsonAdapter
class EnumExample(
        val values: Array<EnumValue>,
        val valuesWithDefault: Array<EnumValueWithDefault>
) {

    @EnumGsonAdapter(fieldNamingPolicy = [FieldNamingPolicy.LOWER_CASE_WITH_DASHES])
    enum class EnumValue {
        VALUE1,
        VALUE_2,
        VALUE_3_AND_4
    }

    @EnumGsonAdapter(fieldNamingPolicy = [FieldNamingPolicy.LOWER_CASE_WITH_DASHES])
    enum class EnumValueWithDefault {
        @EnumGsonAdapter.DefaultValue
        VALUE1
    }
}