package generator.extension.gson_sub_type.with_other_elements;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
class TypesList {
    String other1;

    @TypeGsonSubType
    Type items;

    String other2;
}
