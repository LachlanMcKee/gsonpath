package generator.extension.gson_sub_type.failures;

import generator.extension.gson_sub_type.Type1;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_BlankFieldName {
    @GsonSubtype(
            subTypeKey = "",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Type1.class)
            }
    )
    Type[] items;
}
