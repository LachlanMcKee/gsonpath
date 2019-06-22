package generator.extension.gson_sub_type.default_value;

import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        subTypeFailureOutcome = GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE,
        defaultType = Type2.class,
        stringValueSubtypes = {
                @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class)
        }
)
public @interface TypeGsonSubType {
}