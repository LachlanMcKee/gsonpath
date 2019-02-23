package generator.extension.gson_sub_type.with_other_elements;

import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        stringValueSubtypes = {
                @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                @GsonSubtype.StringValueSubtype(value = "type2", subtype = Type2.class)
        }
)
public @interface TypeGsonSubType {
}