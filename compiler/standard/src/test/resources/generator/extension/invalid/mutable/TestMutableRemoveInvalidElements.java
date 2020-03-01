package generator.standard.invalid.mutable;

import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.extension.annotation.RemoveInvalidElements;

import java.util.List;

@AutoGsonAdapter
public class TestMutableRemoveInvalidElements {
    @RemoveInvalidElements
    String[] value1;

    @RemoveInvalidElements
    List<String> value2;
}