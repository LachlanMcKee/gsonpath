package generator.interf.inheritance;

import com.google.gson.annotations.SerializedName;

public interface TestUsingInheritanceBase {
    @SerializedName("Json1.Nest1")
    Integer getValue1();

    Integer getValue2();

    Integer getValue3();
}