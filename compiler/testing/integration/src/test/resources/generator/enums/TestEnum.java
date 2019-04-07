package generator.enums;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
enum TestEnum {
    VALUE_ABC,
    VALUE_DEF,
    @SerializedName("custom")
    VALUE_GHI,
    VALUE_1
}