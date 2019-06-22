package generator.extension.gson_sub_type.failure_outcome_remove_element;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
class TypesList {
    @TypeGsonSubType
    Type items;
}
