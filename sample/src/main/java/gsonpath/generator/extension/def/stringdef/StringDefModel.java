package gsonpath.generator.extension.def.stringdef;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface StringDefModel {
    @StringDefExample
    String getValue();
}

