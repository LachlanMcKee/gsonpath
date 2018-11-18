package generator.standard.polymorphism.integer_keys;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.standard.polymorphism.Type;
import generator.standard.polymorphism.Type1;
import generator.standard.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            integerValueSubtypes = {
                    @GsonSubtype.IntegerValueSubtype(value = 0, subtype = Type1.class),
                    @GsonSubtype.IntegerValueSubtype(value = 1, subtype = Type2.class)
            }
    )
    Type[] items;
}
