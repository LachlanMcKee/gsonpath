package generator.gson_sub_type.failures;

import gsonpath.GsonSubtype;

@GsonSubtype(
        jsonKeys = {"type"}
)
public class TypesList_NoGsonSubtypeGetter {
}