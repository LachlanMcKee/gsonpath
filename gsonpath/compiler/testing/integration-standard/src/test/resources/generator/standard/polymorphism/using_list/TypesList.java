package generator.standard.polymorphism.using_list;

import java.util.List;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.standard.polymorphism.Type;
import generator.standard.polymorphism.Type1;
import generator.standard.polymorphism.Type2;

@AutoGsonAdapter
class TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                    @GsonSubtype.StringValueSubtype(value = "type2", subtype = Type2.class)
            }
    )
    List<Type> items;
}
