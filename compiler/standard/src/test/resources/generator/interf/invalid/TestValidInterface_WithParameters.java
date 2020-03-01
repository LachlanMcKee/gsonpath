package generator.interf.invalid;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public interface TestValidInterface_WithParameters {
    int getInvalid(int invalidParameter);
}