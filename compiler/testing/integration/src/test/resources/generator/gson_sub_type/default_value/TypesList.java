package generator.extension.gson_sub_type.default_value;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
class TypesList {
    @TypeGsonSubType
    Type[] items;
}
