package generator.standard.flatten.invalid;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.FlattenJson;

@AutoGsonAdapter
public class TestInvalidFlattenJson {
    @FlattenJson
    public Integer value1;
}