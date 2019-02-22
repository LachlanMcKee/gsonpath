package generator.extension.gson_sub_type.boolean_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;

@GsonSubtype(
    subTypeKey = "type",
    booleanValueSubtypes = {
        @GsonSubtype.BooleanValueSubtype(value = true, subtype = Type1.class),
        @GsonSubtype.BooleanValueSubtype(value = false, subtype = Type2.class)
    }
)
public @interface TypeGsonSubType {
}