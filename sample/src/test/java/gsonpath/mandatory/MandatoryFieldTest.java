package gsonpath.mandatory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import gsonpath.JsonFieldNoKeyException;
import gsonpath.TestGsonTypeFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MandatoryFieldTest {
    @Test
    public void test() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new TestGsonTypeFactoryImpl(null));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("MandatoryTestJson.json");

        Gson gson = builder.create();

        try {
            gson.fromJson(new InputStreamReader(resourceAsStream), MandatorySampleModel.class);

        } catch (JsonParseException e) {
            // Since the mandatory value is not found, we are expecting an exception.
            Assert.assertEquals(e.getClass(), JsonFieldNoKeyException.class);
            return;
        }

        Assert.fail("Expected JsonFieldMissingException was not triggered");
    }
}
