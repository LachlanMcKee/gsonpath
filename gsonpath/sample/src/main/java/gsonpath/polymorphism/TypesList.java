package gsonpath.polymorphism;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
interface TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                    @GsonSubtype.StringValueSubtype(value = "type2", subtype = Type2.class),
                    @GsonSubtype.StringValueSubtype(value = "type3", subtype = Type3.class)
            }
    )
    Type[] getItems();
}
