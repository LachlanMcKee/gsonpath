package generator.standard.polymorphism.failures;

import gsonpath.AutoGsonAdapter;
import gsonpath.GsonSubtype;

@AutoGsonAdapter
class TypesList_NoKeys {
    @GsonSubtype(
            subTypeKey = "type"
    )
    Type[] items;
}
