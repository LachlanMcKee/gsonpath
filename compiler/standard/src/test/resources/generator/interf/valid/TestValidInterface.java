package generator.interf.valid;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public interface TestValidInterface {
    @SerializedName("Json1.Nest1")
    Integer getValue1();

    Integer getValue2();

    @SerializedName("Json1.Nest3")
    Integer getValue3();

    Integer getResult();

    Integer getThat();
}