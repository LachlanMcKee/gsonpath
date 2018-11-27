package generator.standard.size.valid.nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.Size;

@AutoGsonAdapter
public class TestImmutableSize {
    @Size(min = 0, max = 6, multiple = 2)
    private String[] value1;

    public TestImmutableSize(String[] value1) {
        this.value1 = value1;
    }

    public String[] getValue1() {
        return value1;
    }
}