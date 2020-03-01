package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestInvalidFlattenJsonError {
    @SerializedName("element1")
    public Integer element1;
}