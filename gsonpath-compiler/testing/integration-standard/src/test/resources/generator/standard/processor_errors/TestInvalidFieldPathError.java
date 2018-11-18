package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestInvalidFieldPathError {
    public double value;

    /**
     * The error happens within this annotation since we assume that 'value' is a json object,
     * and yet we assume that 'value' is a double in the previous field.
     */
    @SerializedName("value.record")
    public double record;
}