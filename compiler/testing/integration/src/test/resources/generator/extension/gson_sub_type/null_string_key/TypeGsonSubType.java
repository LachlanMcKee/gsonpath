package generator.extension.gson_sub_type.null_string_key;

import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        defaultType = Type1.class,
        stringValueSubtypes = {
                @GsonSubtype.StringValueSubtype(value = GsonSubtype.StringValueSubtype.NULL_STRING, subtype = Type2.class)
        }
)
public @interface TypeGsonSubType {
}