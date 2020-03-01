package generator.standard.substitution.valid;

import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.annotation.PathSubstitution;

@AutoGsonAdapter(substitutions = {
        @PathSubstitution(original = "SUB_1", replacement = "Impl1_A"),
        @PathSubstitution(original = "SUB_2", replacement = "Impl1_B")
})
public class TestSubstitutionImpl1 extends TestSubstitutionBase {
}