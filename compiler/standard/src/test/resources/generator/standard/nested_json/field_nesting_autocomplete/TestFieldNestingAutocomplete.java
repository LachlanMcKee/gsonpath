package generator.standard.nested_json.field_nesting_autocomplete;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public class TestFieldNestingAutocomplete {
    @SerializedName("Json1.")
    public int value1;
}