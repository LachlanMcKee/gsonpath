package generator.standard.nested_json.field_nesting_autocomplete_inheritance;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestFieldNestingAutocomplete implements TestFieldNestingAutocompleteBase {
    private int value1;

    public TestFieldNestingAutocomplete(int value1) {
        this.value1 = value1;
    }

    @Override
    public int getValue1() {
        return value1;
    }
}