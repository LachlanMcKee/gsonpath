package gsonpath.kotlin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class SealedClassSampleTest {
    @Test
    public void testWithAllValues() {
        SealedClassSampleWithArray model = runTest(SealedClassSampleWithArray.class, "SealedClassSample.json");

        Type item1 = model.getItems()[0];
        Assert.assertEquals("Type1 Example", item1.getName());
        Assert.assertEquals(1, ((Type.Type1) item1).getIntTest());

        Type item2 = model.getItems()[1];
        Assert.assertEquals("Type2 Example", item2.getName());
        Assert.assertEquals(1.0, ((Type.Type2) item2).getDoubleTest(), 0);

        Type item3 = model.getItems()[2];
        Assert.assertEquals("Type3 Example", item3.getName());
        Assert.assertEquals("123", ((Type.Type3) item3).getStringTest());
    }

    @Test
    public void testSinglePojo() {
        SealedClassSampleWithPojo model = runTest(SealedClassSampleWithPojo.class, "SealedClassSample.json");

        Type item1 = model.getItem();
        Assert.assertEquals("Type1 Example", item1.getName());
        Assert.assertEquals(1, ((Type.Type1) item1).getIntTest());
    }

    private <T> T runTest(Class<T> clazz, String fileName) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);

        Gson gson = builder.create();
        return gson.fromJson(new InputStreamReader(resourceAsStream), clazz);
    }
}
