package generator.interf.primitive;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public interface TestUsingPrimitives {
    int getIntExample();
    long getLongExample();
    double getDoubleExample();
    boolean getBooleanExample();
    int[] getIntArrayExample();
    long[] getLongArrayExample();
    double[] getDoubleArrayExample();
    boolean[] getBooleanArrayExample();
}