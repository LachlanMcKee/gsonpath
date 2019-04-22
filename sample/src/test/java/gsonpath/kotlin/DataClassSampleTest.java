package gsonpath.kotlin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.GsonPath;
import gsonpath.JsonFieldMissingException;
import gsonpath.JsonFieldNoKeyException;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.fail;

public class DataClassSampleTest {
    @Test
    public void testWithAllValues() {
        DataClassSample model = runTest("DataClassJson_all_values.json");

        Assert.assertEquals("test", model.getValue1());
        Assert.assertFalse(model.isBooleanTest1());
        Assert.assertNull(model.isBooleanTest2());
    }

    @Test
    public void testWithMissingValues() {
        try {
            runTest("DataClassJson_missing_values.json");
            fail("Exception not triggered");
        } catch (Exception e) {
            Assert.assertEquals(JsonFieldNoKeyException.class, e.getClass());
            Assert.assertEquals("Mandatory JSON element 'parent.child.value1' was not found within class " +
                "'gsonpath.kotlin.DataClassSample'", e.getMessage());
        }
    }

    private DataClassSample runTest(String fileName) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);

        Gson gson = builder.create();
        return gson.fromJson(new InputStreamReader(resourceAsStream), DataClassSample.class);
    }
}
