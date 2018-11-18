package generator.standard.delimiter.multiple;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;

@AutoGsonAdapter(flattenDelimiter = '$')
public class TestMultipleDelimiters {
    @SerializedName("Json1$Nest1")
    public int value1;

    @SerializedName("Json2.Nest1")
    public int value2;
}