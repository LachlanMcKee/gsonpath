package generator.standard.substitution.valid;

import gsonpath.AutoGsonAdapter;
import gsonpath.PathSubstitution;

@AutoGsonAdapter(substitutions = {
        @PathSubstitution(original = "SUB_1", replacement = "Impl2_A"),
        @PathSubstitution(original = "SUB_2", replacement = "Impl2_B")
})
public class TestSubstitutionImpl2 extends TestSubstitutionBase {
}