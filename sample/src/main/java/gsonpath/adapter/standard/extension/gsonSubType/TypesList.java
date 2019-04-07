package gsonpath.adapter.standard.extension.gsonSubType;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesList {
    @TypeGsonSubType
    Type[] getItems();
}
