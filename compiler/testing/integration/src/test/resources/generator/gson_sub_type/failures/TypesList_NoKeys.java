package generator.extension.gson_sub_type.failures;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_NoKeys {
    @GsonSubtype(
            subTypeKey = "type"
    )
    Type[] items;
}
