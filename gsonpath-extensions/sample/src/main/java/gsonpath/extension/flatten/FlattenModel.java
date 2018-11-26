package gsonpath.extension.flatten;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.FlattenJson;
import gsonpath.extension.def.intdef.IntDefExample;

@AutoGsonAdapter
interface FlattenModel {
    @FlattenJson
    String getValue();
}
