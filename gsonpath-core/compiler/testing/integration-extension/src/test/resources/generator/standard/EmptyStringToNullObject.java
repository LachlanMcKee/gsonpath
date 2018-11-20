package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.FlattenJson;
import gsonpath.extension.EmptyStringToNull;

@AutoGsonAdapter
public class EmptyStringToNullObject {
    @FlattenJson
    @EmptyStringToNull
    public String element1;

    @EmptyStringToNull
    public String element2;
}