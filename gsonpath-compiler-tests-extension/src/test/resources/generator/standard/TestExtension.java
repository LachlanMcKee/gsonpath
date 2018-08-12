package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.ExcludeField;

@AutoGsonAdapter
public class TestExtension {
    public int element1;
    @ExcludeField
    public int element2;
}