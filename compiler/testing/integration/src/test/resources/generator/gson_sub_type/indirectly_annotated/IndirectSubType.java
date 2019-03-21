package generator.gson_sub_type.indirectly_annotated;

import gsonpath.GsonSubTypeFailureOutcome;
import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        booleanValueSubtypes = {
                @GsonSubtype.BooleanValueSubtype(value = true, subtype = IndirectlyAnnotatedSubType.Type1.class),
                @GsonSubtype.BooleanValueSubtype(value = false, subtype = IndirectlyAnnotatedSubType.Type2.class)
        }
)
public @interface IndirectSubType {
}