package generator.gson_sub_type.directly_annotated;

import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        booleanValueSubtypes = {
                @GsonSubtype.BooleanValueSubtype(value = true, subtype = DirectlyAnnotatedSubType.Type1.class),
                @GsonSubtype.BooleanValueSubtype(value = false, subtype = DirectlyAnnotatedSubType.Type2.class)
        }
)
public abstract class DirectlyAnnotatedSubType {
    String name;

    public class Type1 extends DirectlyAnnotatedSubType {
        int intTest;
    }

    public class Type2 extends DirectlyAnnotatedSubType {
        double doubleTest;
    }
}
