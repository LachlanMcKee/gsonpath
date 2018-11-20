package generator.standard.delimiter.custom;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;

@AutoGsonAdapter(flattenDelimiter = '$')
public class TestCustomDelimiter {
    @SerializedName("Json1$Nest1")
    public int value1;
}