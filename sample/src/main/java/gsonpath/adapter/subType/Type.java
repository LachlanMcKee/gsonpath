package gsonpath.adapter.subType;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;
import gsonpath.GsonSubtypeGetter;
import gsonpath.Nullable;

@GsonSubtype(
        jsonKeys = {"type"}
)
public abstract class Type {

    @GsonSubtypeGetter
    static Class<? extends Type> getSubType(@Nullable String type) {
        if (type == null) {
            return TypeNull.class;
        }
        switch (type) {
            case "type1":
                return Type1.class;
            case "type2":
                return Type2.class;
            case "type3":
                return Type3.class;
        }
        return null;
    }

    String type;
    String name;

    @AutoGsonAdapter
    public static class Type1 extends Type {
        int intTest;
    }

    @AutoGsonAdapter
    public static class Type2 extends Type {
        double doubleTest;
    }

    @AutoGsonAdapter
    public static class Type3 extends Type {
        String stringTest;
    }

    @AutoGsonAdapter
    public static class TypeNull extends Type {
    }
}
