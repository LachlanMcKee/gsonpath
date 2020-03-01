package generator.standard.field_annotations.exclude;

import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.annotation.ExcludeField;

@AutoGsonAdapter
public class TestExclude {
    public int element1;
    @ExcludeField
    public int element2;
}