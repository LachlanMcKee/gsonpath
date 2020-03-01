package generator.standard.naming_policy.identity;

import com.google.gson.FieldNamingPolicy;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.IDENTITY)
public class TestNamePolicyIdentity {
    public int testValue;
}