package generator.standard.custom_serialized_name_annotation;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestCustomSerializedNameModel {
    @CustomSerializedName
    String value1;

    @CustomSerializedName
    String value2;
}