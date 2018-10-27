package generator.interf.java8;

import java.lang.Void;

import gsonpath.AutoGsonAdapter;
import com.google.gson.annotations.SerializedName;

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