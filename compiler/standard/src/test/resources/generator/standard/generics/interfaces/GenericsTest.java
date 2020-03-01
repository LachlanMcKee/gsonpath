package generator.standard.generics.interfaces;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
interface GenericsTest extends IntermediateTest<String, Double> {
}