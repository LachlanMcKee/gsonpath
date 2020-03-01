package generator.standard.field_policy.no_validation;

import gsonpath.GsonFieldValidationType;
import gsonpath.NonNull;
import gsonpath.Nullable;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.NO_VALIDATION)
public class TestNoValidation {
    @NonNull // This annotation is not used for validation purposes
    public Integer optional1;
    @Nullable // This annotation is not used for validation purposes
    public Integer optional2;

    public int optional3;
}