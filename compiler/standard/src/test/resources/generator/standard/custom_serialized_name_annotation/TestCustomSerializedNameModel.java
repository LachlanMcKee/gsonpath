package generator.standard.custom_serialized_name_annotation;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;
import gsonpath.NestedJson;

@AutoGsonAdapter(ignoreNonAnnotatedFields = true)
public class TestCustomSerializedNameModel {
    @GsonNest
    String value1;

    @GsonNest
    @SerializedName("value2")
    String valueX;

    @GsonNest
    @SerializedName("second.")
    String value3;

    @NestedJson("nest")
    String value1b;

    @NestedJson("nest")
    @SerializedName("value2b")
    String valueXb;

    @NestedJson("nest")
    @SerializedName("second.")
    String value3b;
}