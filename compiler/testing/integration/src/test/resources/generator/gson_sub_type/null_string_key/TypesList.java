package generator.extension.gson_sub_type.null_string_key;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;

@AutoGsonAdapter
class TypesList {
    @TypeGsonSubType
    Type[] items;
}
