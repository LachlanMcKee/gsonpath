package generator.standard.size.valid.nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.Size;

@AutoGsonAdapter
public class TestMutableSize {
    @Size(min = 0, max = 6, multiple = 2)
    String[] value1;
}