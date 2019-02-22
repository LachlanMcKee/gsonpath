package generator.extension.gson_sub_type.using_interface;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;

@AutoGsonAdapter
interface TypesList {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class),
                    @GsonSubtype.StringValueSubtype(value = "type2", subtype = Type2.class)
            }
    )
    Type[] getItems();
}
