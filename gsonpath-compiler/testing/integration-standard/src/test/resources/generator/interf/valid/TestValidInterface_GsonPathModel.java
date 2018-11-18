package generator.interf.valid;

import com.google.gson.annotations.SerializedName;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestValidInterface_GsonPathModel implements TestValidInterface {
    private final Integer value1;

    private final Integer value2;

    private final Integer value3;

    private final Integer result;

    private final Integer that;

    public TestValidInterface_GsonPathModel(Integer value1, Integer value2, Integer value3, Integer result, Integer that) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.result = result;
        this.that = that;
    }

    @Override
    @SerializedName("Json1.Nest1")
    public Integer getValue1() {
        return value1;
    }

    @Override
    public Integer getValue2() {
        return value2;
    }

    @Override
    @SerializedName("Json1.Nest3")
    public Integer getValue3() {
        return value3;
    }

    @Override
    public Integer getResult() {
        return result;
    }

    @Override
    public Integer getThat() {
        return that;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestValidInterface_GsonPathModel equalsOtherType = (TestValidInterface_GsonPathModel) o;

        if (value1 != null ? !value1.equals(equalsOtherType.value1) : equalsOtherType.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(equalsOtherType.value2) : equalsOtherType.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(equalsOtherType.value3) : equalsOtherType.value3 != null) {
            return false;
        }
        if (result != null ? !result.equals(equalsOtherType.result) : equalsOtherType.result != null) {
            return false;
        }
        if (that != null ? !that.equals(equalsOtherType.that) : equalsOtherType.that != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCodeReturnValue = value1 != null ? value1.hashCode() : 0;
        hashCodeReturnValue = 31 * hashCodeReturnValue + (value2 != null ? value2.hashCode() : 0);
        hashCodeReturnValue = 31 * hashCodeReturnValue + (value3 != null ? value3.hashCode() : 0);
        hashCodeReturnValue = 31 * hashCodeReturnValue + (result != null ? result.hashCode() : 0);
        hashCodeReturnValue = 31 * hashCodeReturnValue + (that != null ? that.hashCode() : 0);
        return hashCodeReturnValue;
    }

    @Override
    public String toString() {
        return "TestValidInterface{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                ", result=" + result +
                ", that=" + that +
                '}';
    }
}