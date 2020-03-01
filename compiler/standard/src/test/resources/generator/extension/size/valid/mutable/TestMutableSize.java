package generator.standard.size.valid.nullable;

import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.extension.annotation.Size;

@AutoGsonAdapter
public class TestMutableSize {
    @Size(value = 1)
    String[] value1;
}