package generator.standard.substitution.valid;

import com.google.gson.annotations.SerializedName;

public class TestSubstitutionBase {
    @SerializedName("{SUB_1}.Json1")
    public int value1;
    @SerializedName("{SUB_2}.Json1")
    public int value2;
}