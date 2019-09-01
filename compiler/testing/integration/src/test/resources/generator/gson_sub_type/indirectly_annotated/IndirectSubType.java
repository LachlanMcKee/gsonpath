package generator.gson_sub_type.indirectly_annotated;

import gsonpath.GsonSubtype;

@GsonSubtype(
        jsonKeys = {"type"}
)
public @interface IndirectSubType {
}