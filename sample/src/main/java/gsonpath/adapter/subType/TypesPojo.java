package gsonpath.adapter.subType;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesPojo {
    @SerializedName("items[0]")
    Type getItem0();

    @SerializedName("items[1]")
    Type getItem1();

    @SerializedName("items[2]")
    Type getItem2();

    @SerializedName("items[3]")
    Type getItem3();

    @SerializedName("items[4]")
    Type getItem4();
}
