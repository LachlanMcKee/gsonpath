package generator.interf.java8;

import gsonpath.GsonPathGenerated;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestJava8Interface_GsonPathModel implements TestJava8Interface {
    private final Integer value1;

    public TestJava8Interface_GsonPathModel(Integer value1) {
        this.value1 = value1;
    }

    @Override
    public Integer getValue1() {
        return value1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestJava8Interface_GsonPathModel equalsOtherType = (TestJava8Interface_GsonPathModel) o;

        if (value1 != null ? !value1.equals(equalsOtherType.value1) : equalsOtherType.value1 != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCodeReturnValue = value1 != null ? value1.hashCode() : 0;
        return hashCodeReturnValue;
    }

    @Override
    public String toString() {
        return "TestJava8Interface{" +
                "value1=" + value1 +
                '}';
    }
}