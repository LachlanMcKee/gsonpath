package generator.enums.without_default;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.EnumGsonAdapter;

@EnumGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
enum TestEnumWithoutDefault {
    VALUE_ABC,
    VALUE_DEF,
    @SerializedName("custom")
    VALUE_GHI,
    VALUE_1
}