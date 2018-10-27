package generator.standard.naming_policy.identity;

import com.google.gson.FieldNamingPolicy;
import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.IDENTITY)
public class TestNamePolicyIdentity {
    public int testValue;
}