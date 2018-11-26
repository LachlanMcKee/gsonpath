package generator.standard.flatten.valid.nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import gsonpath.extension.annotation.FlattenJson;

@AutoGsonAdapter
public class TestImmutableFlattenJson {
    @FlattenJson
    private String value1;

    public TestImmutableFlattenJson(String value1) {
        this.value1 = value1;
    }

    public String getValue1() {
        return value1;
    }
}