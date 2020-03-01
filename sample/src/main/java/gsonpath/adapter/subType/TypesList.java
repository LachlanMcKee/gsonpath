package gsonpath.adapter.subType;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesList {
    Type[] getItems();
}
