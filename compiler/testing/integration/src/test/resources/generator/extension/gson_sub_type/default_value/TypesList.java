package generator.extension.gson_sub_type.default_value;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            subTypeFailureOutcome = GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE,
            defaultType = Type2.class,
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class)
            }
    )
    Type[] items;
}
