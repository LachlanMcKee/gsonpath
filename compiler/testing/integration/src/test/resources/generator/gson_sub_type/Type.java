package generator.gson_sub_type;

import gsonpath.GsonSubtype;

@GsonSubtype(
        subTypeKey = "type",
        booleanValueSubtypes = {
                @GsonSubtype.BooleanValueSubtype(value = true, subtype = Type.Type1.class),
                @GsonSubtype.BooleanValueSubtype(value = false, subtype = Type.Type2.class)
        }
)
public abstract class Type {
    String name;

    public class Type1 extends Type {
        int intTest;
    }

    public class Type2 extends Type {
        double doubleTest;
    }
}
