package generator.standard.polymorphism.default_value;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

import generator.standard.polymorphism.Type;
import generator.standard.polymorphism.Type1;
import generator.standard.polymorphism.Type2;

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
