package generator.interf.flatten_json;

import gsonpath.AutoGsonAdapter;
import gsonpath.FlattenJson;

@AutoGsonAdapter
public interface TestFlattenJsonWithInterface {
    @FlattenJson
    String getFlattenExample();
}