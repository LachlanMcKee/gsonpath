package generator.standard.generics.interfaces_and_classs;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
class GenericsTest extends IntermediateTest<String, Double> {
    Double value3;

    @Override
    public Double getValue3() {
        return value3;
    }
}