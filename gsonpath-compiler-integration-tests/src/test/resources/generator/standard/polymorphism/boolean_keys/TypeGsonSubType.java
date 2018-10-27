package generator.standard.polymorphism.boolean_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.standard.polymorphism.Type;
import generator.standard.polymorphism.Type1;
import generator.standard.polymorphism.Type2;

@GsonSubtype(
    subTypeKey = "type",
    booleanValueSubtypes = {
        @GsonSubtype.BooleanValueSubtype(value = true, subtype = Type1.class),
        @GsonSubtype.BooleanValueSubtype(value = false, subtype = Type2.class)
    }
)
public @interface TypeGsonSubType {
}