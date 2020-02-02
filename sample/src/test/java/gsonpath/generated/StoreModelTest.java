package gsonpath.generated;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.TestGsonTypeFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StoreModelTest {
    @Test
    public void test() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new TestGsonTypeFactoryImpl(null));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("BookJson.json");

        Gson gson = builder.create();
        StoreModel model = gson.fromJson(new InputStreamReader(resourceAsStream), StoreModel.class);

        Assert.assertEquals("red", model.bikeColour);
        Assert.assertEquals(4, model.bookList.size());
        Assert.assertEquals("J. R. R. Tolkien", model.bookList.get(3).author);
    }
}
