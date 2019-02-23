package gsonpath.generator.extension.gsonSubType;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesList {
    @TypeGsonSubType
    Type[] getItems();
}
