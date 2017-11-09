package gsonpath.interface_test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStreamReader;

public class InterfaceEqualityTest {
    @Test
    public void onAssertEquals_givenSameJson_expectEqual() {
        compareJsonFiles("InterfaceEqualityVariant1TestJson.json", "InterfaceEqualityVariant1TestJson.json", true);
    }

    @Test
    public void onAssertEquals_givenDifferentJson_expectNotEqual() {
        compareJsonFiles("InterfaceEqualityVariant1TestJson.json", "InterfaceEqualityVariant2TestJson.json", false);
    }

    @Test
    public void givenModelCreated_whenToJsonInvoked_thenExpectJsonTextOutput() {
        Gson gson = buildGson();

        InterfaceExample interfaceExample = InterfaceExample.create(
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        String jsonText = gson.toJson(interfaceExample);
        Assert.assertEquals("Check JSON text exists", "{\"intExample\":1}", jsonText);
    }

    private void compareJsonFiles(String filename1, String filename2, boolean expectEqual) {
        Gson gson = buildGson();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InterfaceExample sample1 = gson.fromJson(new InputStreamReader(classLoader.getResourceAsStream(filename1)), InterfaceExample.class);
        InterfaceExample sample2 = gson.fromJson(new InputStreamReader(classLoader.getResourceAsStream(filename2)), InterfaceExample.class);

        if (expectEqual) {
            Assert.assertEquals(sample1, sample2);
        } else {
            Assert.assertNotEquals(sample1, sample2);
        }
    }

    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));
        return builder.create();
    }

}
