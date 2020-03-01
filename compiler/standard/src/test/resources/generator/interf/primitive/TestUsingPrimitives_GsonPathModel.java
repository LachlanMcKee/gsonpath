package generator.interf.primitive;

import gsonpath.annotation.GsonPathGenerated;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestUsingPrimitives_GsonPathModel implements TestUsingPrimitives {
    private final int intExample;

    private final long longExample;

    private final double doubleExample;

    private final boolean booleanExample;

    private final int[] intArrayExample;

    private final long[] longArrayExample;

    private final double[] doubleArrayExample;

    private final boolean[] booleanArrayExample;

    public TestUsingPrimitives_GsonPathModel(int intExample, long longExample, double doubleExample,
                                             boolean booleanExample, int[] intArrayExample, long[] longArrayExample,
                                             double[] doubleArrayExample, boolean[] booleanArrayExample) {
        this.intExample = intExample;
        this.longExample = longExample;
        this.doubleExample = doubleExample;
        this.booleanExample = booleanExample;
        this.intArrayExample = intArrayExample;
        this.longArrayExample = longArrayExample;
        this.doubleArrayExample = doubleArrayExample;
        this.booleanArrayExample = booleanArrayExample;
    }

    @Override
    public int getIntExample() {
        return intExample;
    }

    @Override
    public long getLongExample() {
        return longExample;
    }

    @Override
    public double getDoubleExample() {
        return doubleExample;
    }

    @Override
    public boolean getBooleanExample() {
        return booleanExample;
    }

    @Override
    public int[] getIntArrayExample() {
        return intArrayExample;
    }

    @Override
    public long[] getLongArrayExample() {
        return longArrayExample;
    }

    @Override
    public double[] getDoubleArrayExample() {
        return doubleArrayExample;
    }

    @Override
    public boolean[] getBooleanArrayExample() {
        return booleanArrayExample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestUsingPrimitives_GsonPathModel equalsOtherType = (TestUsingPrimitives_GsonPathModel) o;

        if (intExample != equalsOtherType.intExample) {
            return false;
        }
        if (longExample != equalsOtherType.longExample) {
            return false;
        }
        if (doubleExample != equalsOtherType.doubleExample) {
            return false;
        }
        if (booleanExample != equalsOtherType.booleanExample) {
            return false;
        }
        if (!java.util.Arrays.equals(intArrayExample, equalsOtherType.intArrayExample)) {
            return false;
        }
        if (!java.util.Arrays.equals(longArrayExample, equalsOtherType.longArrayExample)) {
            return false;
        }
        if (!java.util.Arrays.equals(doubleArrayExample, equalsOtherType.doubleArrayExample)) {
            return false;
        }
        if (!java.util.Arrays.equals(booleanArrayExample, equalsOtherType.booleanArrayExample)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long temp;
        int hashCodeReturnValue = intExample;
        hashCodeReturnValue = 31 * hashCodeReturnValue + ((int) (longExample ^ (longExample >>> 32)));
        temp = java.lang.Double.doubleToLongBits(doubleExample);
        hashCodeReturnValue = 31 * hashCodeReturnValue + ((int) (temp ^ (temp >>> 32)));
        hashCodeReturnValue = 31 * hashCodeReturnValue + ((booleanExample ? 1 : 0));
        hashCodeReturnValue = 31 * hashCodeReturnValue + (java.util.Arrays.hashCode(intArrayExample));
        hashCodeReturnValue = 31 * hashCodeReturnValue + (java.util.Arrays.hashCode(longArrayExample));
        hashCodeReturnValue = 31 * hashCodeReturnValue + (java.util.Arrays.hashCode(doubleArrayExample));
        hashCodeReturnValue = 31 * hashCodeReturnValue + (java.util.Arrays.hashCode(booleanArrayExample));
        return hashCodeReturnValue;
    }

    @Override
    public String toString() {
        return "TestUsingPrimitives{" +
                "intExample=" + intExample +
                ", longExample=" + longExample +
                ", doubleExample=" + doubleExample +
                ", booleanExample=" + booleanExample +
                ", intArrayExample=" + java.util.Arrays.toString(intArrayExample) +
                ", longArrayExample=" + java.util.Arrays.toString(longArrayExample) +
                ", doubleArrayExample=" + java.util.Arrays.toString(doubleArrayExample) +
                ", booleanArrayExample=" + java.util.Arrays.toString(booleanArrayExample) +
                '}';
    }
}