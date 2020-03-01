package generator.standard.field_policy.validate_explicit_non_null;

import gsonpath.GsonFieldValidationType;
import gsonpath.NonNull;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
public class TestValidateExplicitNonNull {
    @NonNull
    public Integer mandatory1;
    @NonNull
    public Integer mandatory2;
    public int mandatory3;

    public Integer optional1;
}