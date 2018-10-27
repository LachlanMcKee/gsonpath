package generator.interf.invalid;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public interface TestValidInterface_WithParameters {
    int getInvalid(int invalidParameter);
}