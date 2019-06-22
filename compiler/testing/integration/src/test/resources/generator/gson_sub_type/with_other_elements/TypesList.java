package generator.extension.gson_sub_type.with_other_elements;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;

@AutoGsonAdapter
class TypesList {
    String other1;

    @TypeGsonSubType
    Type[] items;

    String other2;
}
