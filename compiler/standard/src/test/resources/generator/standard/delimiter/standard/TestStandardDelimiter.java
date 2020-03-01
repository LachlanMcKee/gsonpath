package generator.standard.delimiter.standard;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestStandardDelimiter {
    @SerializedName("Json1.Nest1")
    public int value1;
}