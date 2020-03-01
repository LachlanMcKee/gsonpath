package generator.gson_sub_type.one_argument;

import gsonpath.annotation.GsonSubtype;
import gsonpath.annotation.GsonSubtypeGetter;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypeGsonSubType {
    @GsonSubtypeGetter
    static Class<? extends TypeGsonSubType> getSubType1(String type) {
        return null;
    }
}