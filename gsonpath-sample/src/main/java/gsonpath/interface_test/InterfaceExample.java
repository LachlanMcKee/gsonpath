package gsonpath.interface_test;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
interface InterfaceExample {
    Integer getIntExample();

    Long getLongExample();

    Double getDoubleExample();

    Boolean getBooleanExample();

    Integer[] getIntArrayExample();

    Long[] getLongArrayExample();

    Double[] getDoubleArrayExample();

    Boolean[] getBooleanArrayExample();

    static InterfaceExample create(Integer intExample,
                                   Long longExample,
                                   Double doubleExample,
                                   Boolean booleanExample,
                                   Integer[] intArrayExample,
                                   Long[] longArrayExample,
                                   Double[] doubleArrayExample,
                                   Boolean[] booleanArrayExample) {

        return new InterfaceExample_GsonPathModel(
                intExample,
                longExample,
                doubleExample,
                booleanExample,
                intArrayExample,
                longArrayExample,
                doubleArrayExample,
                booleanArrayExample
        );
    }
}
