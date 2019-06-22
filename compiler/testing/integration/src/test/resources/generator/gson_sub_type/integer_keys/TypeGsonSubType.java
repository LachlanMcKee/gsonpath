package generator.extension.gson_sub_type.integer_keys;

import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        integerValueSubtypes = {
                @GsonSubtype.IntegerValueSubtype(value = 0, subtype = Type1.class),
                @GsonSubtype.IntegerValueSubtype(value = 1, subtype = Type2.class)
        }
)
public @interface TypeGsonSubType {
}