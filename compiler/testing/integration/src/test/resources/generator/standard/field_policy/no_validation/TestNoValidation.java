package generator.standard.field_policy.no_validation;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import gsonpath.NonNull;
import gsonpath.Nullable;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.NO_VALIDATION)
public class TestNoValidation {
    @NonNull // This annotation is not used for validation purposes
    public Integer optional1;
    @Nullable // This annotation is not used for validation purposes
    public Integer optional2;

    public int optional3;
}