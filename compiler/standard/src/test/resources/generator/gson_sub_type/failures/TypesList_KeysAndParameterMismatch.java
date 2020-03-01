package generator.gson_sub_type.failures;

import gsonpath.annotation.GsonSubtype;
import gsonpath.annotation.GsonSubtypeGetter;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypesList_KeysAndParameterMismatch {

    @GsonSubtypeGetter
    static Class<? extends TypesList_KeysAndParameterMismatch> getSubType() {
        return Type1.class;
    }

    public class Type1 extends TypesList_KeysAndParameterMismatch {
    }
}