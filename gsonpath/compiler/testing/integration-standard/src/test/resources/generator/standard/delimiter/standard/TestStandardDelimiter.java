package generator.standard.delimiter.standard;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;

@AutoGsonAdapter
public class TestStandardDelimiter {
    @SerializedName("Json1.Nest1")
    public int value1;
}