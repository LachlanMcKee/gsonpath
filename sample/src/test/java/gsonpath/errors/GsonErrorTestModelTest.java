package gsonpath.errors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import gsonpath.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GsonErrorTestModelTest {
    @Test
    public void test() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new GsonPathTypeAdapterFactory());
        builder.registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("GsonErrorTestJson.json");

        Gson gson = builder.create();
        GsonPathTypeAdapter<GsonErrorTestModel> typeAdapter = (GsonPathTypeAdapter<GsonErrorTestModel>) gson
                .getAdapter(GsonErrorTestModel.class);

        GsonErrors gsonErrors = new GsonErrors();
        GsonErrorTestModel model = typeAdapter
                .read(new JsonReader(new InputStreamReader(resourceAsStream)), gsonErrors);

        Assert.assertEquals(2, model.arrayValue.length);
    }
}
