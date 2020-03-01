package generator.interf.java8;

import gsonpath.annotation.AutoGsonAdapter;

@AutoGsonAdapter
public interface TestJava8Interface {
    Integer getValue1();

    default Void testDefaultIgnored() {
        return null;
    }

    static Void testStaticIgnored() {
        return null;
    }
}