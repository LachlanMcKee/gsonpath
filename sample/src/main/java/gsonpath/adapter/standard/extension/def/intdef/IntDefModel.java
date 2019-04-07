package gsonpath.adapter.standard.extension.def.intdef;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface IntDefModel {
    @IntDefExample
    Integer getValue();
}
