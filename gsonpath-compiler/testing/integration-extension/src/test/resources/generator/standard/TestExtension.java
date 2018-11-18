package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.EmptyStringToNull;

@AutoGsonAdapter
public class TestExtension {
    @EmptyStringToNull
    public String element1;
}