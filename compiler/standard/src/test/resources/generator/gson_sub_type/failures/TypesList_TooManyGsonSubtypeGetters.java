package generator.gson_sub_type.failures;

import gsonpath.GsonSubtype;
import gsonpath.GsonSubtypeGetter;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypesList_TooManyGsonSubtypeGetters {
    @GsonSubtypeGetter
    static Class<? extends TypesList_TooManyGsonSubtypeGetters> getSubType1(boolean type) {
        return null;
    }

    @GsonSubtypeGetter
    static Class<? extends TypesList_TooManyGsonSubtypeGetters> getSubType2(boolean type) {
        return null;
    }
}