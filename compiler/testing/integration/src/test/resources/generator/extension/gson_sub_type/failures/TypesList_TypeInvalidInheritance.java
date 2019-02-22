package generator.extension.gson_sub_type.failures;

import java.lang.String;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_TypeInvalidInheritance {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = String.class)
            }
    )
    Type[] items;
}
