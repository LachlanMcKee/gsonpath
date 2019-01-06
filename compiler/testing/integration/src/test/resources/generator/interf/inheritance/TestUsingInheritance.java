package generator.interf.inheritance;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;
import gsonpath.NonNull;

@AutoGsonAdapter
public interface TestUsingInheritance extends TestUsingInheritanceBase {
    //
    // We remove the old SerializedName and add a non-null override
    //
    @NonNull
    Integer getValue1();

    // We add a new SerializedName when non existed there before.
    @SerializedName("Json1.Nest2")
    Integer getValue2();
}