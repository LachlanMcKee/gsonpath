package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestDuplicateFieldError {
    @SerializedName("value")
    public double value1;

    @SerializedName("value")
    public double value2;
}