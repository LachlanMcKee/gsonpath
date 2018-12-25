package gsonpath.extension.flatten;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.FlattenJson;

@AutoGsonAdapter
interface FlattenModel {
    @FlattenJson
    String getValue();
}
