package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import gsonpath.AutoGsonAdapter

@AutoGsonAdapter
class EnumExample(val values: Array<EnumValue>) {

    @AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
    enum class EnumValue {
        VALUE1,
        VALUE_2,
        VALUE_3_AND_4
    }
}