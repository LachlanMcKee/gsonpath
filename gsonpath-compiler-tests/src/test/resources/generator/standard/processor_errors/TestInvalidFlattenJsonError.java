package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;
import gsonpath.FlattenJson;

@AutoGsonAdapter
public class TestInvalidFlattenJsonError {
    @FlattenJson
    @SerializedName("element1")
    public Integer element1;
}