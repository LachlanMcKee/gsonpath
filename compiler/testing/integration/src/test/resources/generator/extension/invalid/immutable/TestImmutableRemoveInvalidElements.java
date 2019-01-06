package generator.standard.invalid.immutable;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.RemoveInvalidElements;

import java.util.List;

@AutoGsonAdapter
public class TestImmutableRemoveInvalidElements {
    @RemoveInvalidElements
    private String[] value1;

    @RemoveInvalidElements
    private List<String> value2;

    public TestImmutableRemoveInvalidElements(String[] value1, List<String> value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public String[] getValue1() {
        return value1;
    }

    public List<String> getValue2() {
        return value2;
    }
}