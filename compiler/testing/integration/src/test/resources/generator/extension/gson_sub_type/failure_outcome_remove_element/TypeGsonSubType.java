package generator.extension.gson_sub_type.failure_outcome_remove_element;

import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        subTypeFailureOutcome = GsonSubTypeFailureOutcome.REMOVE_ELEMENT,
        stringValueSubtypes = {
                @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                @GsonSubtype.StringValueSubtype(value = "type2", subtype = Type2.class)
        }
)
public @interface TypeGsonSubType {
}