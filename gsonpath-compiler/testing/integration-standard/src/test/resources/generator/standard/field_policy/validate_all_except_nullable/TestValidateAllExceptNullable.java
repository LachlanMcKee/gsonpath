package generator.standard.field_policy.validate_all_except_nullable;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import gsonpath.Nullable;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)
public class TestValidateAllExceptNullable {
    public Integer mandatory1;
    public Integer mandatory2;

    @Nullable
    public Integer optional1;
}