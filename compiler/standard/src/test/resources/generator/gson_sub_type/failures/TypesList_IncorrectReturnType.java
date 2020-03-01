package generator.gson_sub_type.failures;

import gsonpath.annotation.GsonSubtype;
import gsonpath.annotation.GsonSubtypeGetter;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypesList_IncorrectReturnType {

    @GsonSubtypeGetter
    static Class<? extends String> getSubType() {
        return null;
    }
}