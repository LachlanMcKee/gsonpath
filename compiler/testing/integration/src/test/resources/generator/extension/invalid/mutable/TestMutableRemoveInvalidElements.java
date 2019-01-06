package generator.standard.invalid.mutable;

import java.util.List;
import gsonpath.AutoGsonAdapter;
import gsonpath.extension.annotation.RemoveInvalidElements;

@AutoGsonAdapter
public class TestMutableRemoveInvalidElements {
    @RemoveInvalidElements
    String[] value1;

    @RemoveInvalidElements
    List<String> value2;
}