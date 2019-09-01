package generator.gson_sub_type.indirectly_annotated;

import gsonpath.GsonSubtype;
import gsonpath.GsonSubtypeGetter;

@IndirectSubType
public abstract class IndirectlyAnnotatedSubType {
    @GsonSubtypeGetter
    static Class<? extends IndirectlyAnnotatedSubType> getSubType1(boolean type) {
        return null;
    }

    String name;

    public class Type1 extends IndirectlyAnnotatedSubType {
        int intTest;
    }

    public class Type2 extends IndirectlyAnnotatedSubType {
        double doubleTest;
    }
}
