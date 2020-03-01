package generator.gson_sub_type.two_arguments;

import gsonpath.NonNull;
import gsonpath.annotation.GsonSubtype;
import gsonpath.annotation.GsonSubtypeGetter;

import java.util.List;

@GsonSubtype(
        jsonKeys = {"type1", "type2"}
)
public class TypeGsonSubType {
    @GsonSubtypeGetter
    static Class<? extends TypeGsonSubType> getSubType1(@NonNull String type1, List<String> type2) {
        return null;
    }
}