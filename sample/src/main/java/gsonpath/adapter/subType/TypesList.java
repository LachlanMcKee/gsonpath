package gsonpath.adapter.subType;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesList {
    Type[] getItems();
}
