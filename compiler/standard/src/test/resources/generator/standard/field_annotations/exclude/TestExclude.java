package generator.standard.field_annotations.exclude;

import gsonpath.AutoGsonAdapter;
import gsonpath.ExcludeField;

@AutoGsonAdapter
public class TestExclude {
    public int element1;
    @ExcludeField
    public int element2;
}