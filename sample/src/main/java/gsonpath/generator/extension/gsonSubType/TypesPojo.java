package gsonpath.generator.extension.gsonSubType;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface TypesPojo {
    @SerializedName("items[0]")
    @TypeGsonSubType
    Type getItem0();

    @SerializedName("items[1]")
    @TypeGsonSubType
    Type getItem1();

    @SerializedName("items[2]")
    @TypeGsonSubType
    Type getItem2();

    @SerializedName("items[3]")
    @TypeGsonSubType
    Type getItem3();

    @SerializedName("items[4]")
    @TypeGsonSubType
    Type getItem4();
}
