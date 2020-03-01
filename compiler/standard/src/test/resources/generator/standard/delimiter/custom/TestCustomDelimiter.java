package generator.standard.delimiter.custom;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(flattenDelimiter = '$')
public class TestCustomDelimiter {
    @SerializedName("Json1$Nest1")
    public int value1;
}