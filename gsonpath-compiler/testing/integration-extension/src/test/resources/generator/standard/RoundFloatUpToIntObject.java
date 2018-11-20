package generator.standard;

import gsonpath.AutoGsonAdapter;
import gsonpath.extension.RoundFloatUpToInt;

@AutoGsonAdapter
public class RoundFloatUpToIntObject {
    @RoundFloatUpToInt
    public int element1;
}