package generator.standard.empty.ignored_fields;

import gsonpath.AutoGsonAdapter;

@AutoGsonAdapter
public class TestIgnoredFields {
    private static final String TAG = TestIgnoredFields.class.getSimpleName();
    public static final int element1 = 1;
    public final int element2 = 2;
    public static int element3 = 3;
}