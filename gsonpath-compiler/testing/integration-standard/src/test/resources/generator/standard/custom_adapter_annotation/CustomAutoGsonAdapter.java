package generator.standard.custom_adapter_annotation;

import com.google.gson.FieldNamingPolicy;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;

@AutoGsonAdapter(
    flattenDelimiter = '$',
    serializeNulls = true,
    ignoreNonAnnotatedFields = true,
    fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
    fieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE
)
public @interface CustomAutoGsonAdapter {
}