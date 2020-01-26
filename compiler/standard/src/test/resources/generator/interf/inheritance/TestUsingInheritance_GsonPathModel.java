package generator.interf.inheritance;

import com.google.gson.annotations.SerializedName;
import gsonpath.GsonPathGenerated;
import gsonpath.NonNull;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestUsingInheritance_GsonPathModel implements TestUsingInheritance {
    private final Integer value3;
    private final Integer value1;
    private final Integer value2;

    public TestUsingInheritance_GsonPathModel(Integer value3, Integer value1, Integer value2) {
        this.value3 = value3;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public Integer getValue3() {
        return value3;
    }

    @Override
    @NonNull
    public Integer getValue1() {
        return value1;
    }

    @Override
    @SerializedName("Json1.Nest2")
    public Integer getValue2() {
        return value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestUsingInheritance_GsonPathModel equalsOtherType = (TestUsingInheritance_GsonPathModel) o;

        if (value3 != null ? !value3.equals(equalsOtherType.value3) : equalsOtherType.value3 != null) {
            return false;
        }
        if (value1 != null ? !value1.equals(equalsOtherType.value1) : equalsOtherType.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(equalsOtherType.value2) : equalsOtherType.value2 != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCodeReturnValue = value3 != null ? value3.hashCode() : 0;
        hashCodeReturnValue = 31 * hashCodeReturnValue + (value1 != null ? value1.hashCode() : 0);
        hashCodeReturnValue = 31 * hashCodeReturnValue + (value2 != null ? value2.hashCode() : 0);
        return hashCodeReturnValue;
    }

    @Override
    public String toString() {
        return "TestUsingInheritance{" +
                "value3=" + value3 +
                ", value1=" + value1 +
                ", value2=" + value2 +
                '}';
    }
}