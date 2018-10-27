package generator.standard.custom_adapter_annotation;

import com.google.gson.annotations.SerializedName;

@CustomAutoGsonAdapter
public class TestCustomAutoGsonAdapterModel {
    @SerializedName("path$")
    Integer expectedValue;

    String ignored;
}