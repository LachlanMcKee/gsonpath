package generator.standard.field_policy.validate_explicit_non_null;

import gsonpath.GsonFieldValidationType;
import gsonpath.NonNull;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
public class TestValidateWithDefaultValue {
    @NonNull
    public Integer mandatoryWithDefault = 0;
}