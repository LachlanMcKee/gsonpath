package generator.gson_sub_type.failures;

import gsonpath.GsonSubtype;

@GsonSubtype(
        jsonKeys = {"type1", "type1"}
)
public class TypesList_DuplicateKeys {
    public class Type1 extends TypesList_DuplicateKeys {
    }

    public class Type2 extends TypesList_DuplicateKeys {
    }
}