package generator.gson_sub_type.failures;

import gsonpath.GsonSubtype;
import gsonpath.GsonSubtypeGetter;

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