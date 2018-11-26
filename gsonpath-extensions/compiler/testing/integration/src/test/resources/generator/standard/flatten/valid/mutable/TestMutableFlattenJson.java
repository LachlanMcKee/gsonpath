package generator.standard.flatten.valid.nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.FlattenJson;

@AutoGsonAdapter
public class TestMutableFlattenJson {
    @FlattenJson
    public String value1;
}