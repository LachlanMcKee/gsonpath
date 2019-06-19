package generator.extension.gson_sub_type.null_string_key;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
class TypesList {
    @TypeGsonSubType
    Type items;
}
