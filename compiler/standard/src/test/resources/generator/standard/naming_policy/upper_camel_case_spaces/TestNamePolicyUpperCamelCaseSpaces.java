package generator.standard.naming_policy.upper_camel_case_spaces;

import com.google.gson.FieldNamingPolicy;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES)
public class TestNamePolicyUpperCamelCaseSpaces {
    public int testValue;
}