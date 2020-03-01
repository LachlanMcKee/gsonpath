package generator.gson_sub_type.failures;

import gsonpath.annotation.GsonSubtype;

@GsonSubtype(
        jsonKeys = {""}
)
public class TypesList_BlankFieldName {
    public class Type1 extends TypesList_BlankFieldName {
    }
}