package generator.standard.field_policy.validate_explicit_non_null;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import gsonpath.NonNull;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
public class TestValidateWithDefaultValue {
    @NonNull
    public Integer mandatoryWithDefault = 0;
}