package generator.gson_sub_type.indirectly_annotated;

import gsonpath.GsonSubtype;

@IndirectSubType
public abstract class IndirectlyAnnotatedSubType {
    String name;

    public class Type1 extends IndirectlyAnnotatedSubType {
        int intTest;
    }

    public class Type2 extends IndirectlyAnnotatedSubType {
        double doubleTest;
    }
}
