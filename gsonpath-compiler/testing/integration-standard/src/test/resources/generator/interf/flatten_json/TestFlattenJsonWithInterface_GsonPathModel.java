package generator.interf.flatten_json;

import gsonpath.FlattenJson;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestFlattenJsonWithInterface_GsonPathModel implements TestFlattenJsonWithInterface {
    private final String flattenExample;

    public TestFlattenJsonWithInterface_GsonPathModel(String flattenExample) {
        this.flattenExample = flattenExample;
    }

    @Override
    @FlattenJson
    public String getFlattenExample() {
        return flattenExample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestFlattenJsonWithInterface_GsonPathModel equalsOtherType = (TestFlattenJsonWithInterface_GsonPathModel) o;

        if (flattenExample != null ? !flattenExample.equals(equalsOtherType.flattenExample) : equalsOtherType.flattenExample != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCodeReturnValue = flattenExample != null ? flattenExample.hashCode() : 0;
        return hashCodeReturnValue;
    }

    @Override
    public String toString() {
        return "TestFlattenJsonWithInterface{" +
                "flattenExample=" + flattenExample +
                '}';
    }
}