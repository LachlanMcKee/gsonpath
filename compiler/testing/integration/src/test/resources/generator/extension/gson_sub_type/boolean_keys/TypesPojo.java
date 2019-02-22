package generator.extension.gson_sub_type.boolean_keys;

import gsonpath.AutoGsonAdapter;

import generator.extension.gson_sub_type.Type;

@AutoGsonAdapter
class TypesPojo {
    @TypeGsonSubType
    Type items;
}
