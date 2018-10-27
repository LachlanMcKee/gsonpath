package generator.standard.nested_json.field_nesting;

import com.google.gson.annotations.SerializedName;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestFieldNesting {
    @SerializedName("Json1")
    public int value1;

    @SerializedName("Json2.Nest1")
    public int value2;

    @SerializedName("Json2.Nest2.EndPoint1")
    public int value3;

    @SerializedName("Json2.Nest2.EndPoint2")
    public int value4;
}