package generator.gson_sub_type.failures;

import gsonpath.annotation.GsonSubtype;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypesList_NoGsonSubtypeGetter {
}