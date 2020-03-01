package generator.standard.array;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
class TestArray {
    @SerializedName("test1[1]")
    int plainArray;

    @SerializedName("test2[2].child")
    int arrayWithNestedObject;

    @SerializedName("test2[2].child2")
    int arrayWithNestedObject2;

    @SerializedName("test3[3].child[1]")
    int arrayWithNestedArray;

    @SerializedName("test4.child[1]")
    int objectWithNestedArray;
}