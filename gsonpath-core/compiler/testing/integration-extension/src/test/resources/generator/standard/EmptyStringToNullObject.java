package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.EmptyStringToNull;

@AutoGsonAdapter
public class EmptyStringToNullObject {
    @EmptyStringToNull
    public String element1;

    @EmptyStringToNull
    public String element2;
}