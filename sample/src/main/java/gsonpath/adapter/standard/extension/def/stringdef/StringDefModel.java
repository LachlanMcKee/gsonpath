package gsonpath.adapter.standard.extension.def.stringdef;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface StringDefModel {
    @StringDefExample
    String getValue();
}

