package generator.standard.field_policy.validate_all_except_nullable;

import gsonpath.GsonFieldValidationType;
import gsonpath.Nullable;
import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)
public class TestValidateAllExceptNullable {
    public Integer mandatory1;
    public Integer mandatory2;

    @Nullable
    public Integer optional1;
}