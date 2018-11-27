package generator.standard.size.valid.nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.Size;

@AutoGsonAdapter
public class TestMutableSize {
    @Size(value = 1)
    String[] value1;
}