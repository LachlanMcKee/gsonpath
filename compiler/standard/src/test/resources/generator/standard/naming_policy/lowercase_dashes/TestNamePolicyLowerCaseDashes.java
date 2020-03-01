package generator.standard.naming_policy.lowercase_dashes;

import com.google.gson.FieldNamingPolicy;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
public class TestNamePolicyLowerCaseDashes {
    public int testValue;
}