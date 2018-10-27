package generator.standard.substitution.valid;

import gsonpath.AutoGsonAdapter;
import gsonpath.PathSubstitution;

@AutoGsonAdapter(substitutions = {
        @PathSubstitution(original = "KEY_1", replacement = "REPLACE_1"),
        @PathSubstitution(original = "KEY_1", replacement = "REPLACE_2")
})
public class TestSubstitutionError_DuplicateKeys {
}