package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;
import gsonpath.extension.GsonSubtypeCollision;

@AutoGsonAdapter
public class ExtensionWithGsonSubTypeError {
    @GsonSubtype(
            subTypeKey = "type",
            stringValueSubtypes = {
                    @GsonSubtype.StringValueSubtype(value = "type1", subtype = Integer.class),
                    @GsonSubtype.StringValueSubtype(value = "type2", subtype = Long.class)
            }
    )
    @GsonSubtypeCollision
    public Number[] element1;
}