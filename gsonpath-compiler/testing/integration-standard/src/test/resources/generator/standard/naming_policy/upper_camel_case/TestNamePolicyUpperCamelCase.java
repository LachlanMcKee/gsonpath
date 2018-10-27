package generator.standard.naming_policy.upper_camel_case;

import com.google.gson.FieldNamingPolicy;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.UPPER_CAMEL_CASE)
public class TestNamePolicyUpperCamelCase {
    public int testValue;
}