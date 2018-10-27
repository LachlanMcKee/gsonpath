package generator.standard.generics.interfaces_and_classs;

import java.util.Map;

abstract class IntermediateTest<T, V> implements BaseTest<T, String, Integer> {
    T value1;
    Map<String, Integer> value2;

    @Override
    public T getValue1() {
        return value1;
    }

    @Override
    public Map<String, Integer> getValue2() {
        return value2;
    }

    public abstract V getValue3();
}