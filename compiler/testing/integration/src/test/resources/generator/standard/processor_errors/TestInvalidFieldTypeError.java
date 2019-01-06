package generator.standard.processor_errors;

import com.google.gson.annotations.SerializedName;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestInvalidFieldTypeError {
    @SerializedName("element1")
    public java.lang.Object element1;
}