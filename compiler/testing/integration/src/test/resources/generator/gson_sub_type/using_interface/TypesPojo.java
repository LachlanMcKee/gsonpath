package generator.extension.gson_sub_type.using_interface;

import generator.extension.gson_sub_type.Type;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesList {
    @TypeGsonSubType
    Type getItems();
}
