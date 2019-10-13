package gsonpath.safe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gsonpath.GsonPathTypeAdapterFactory;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class GsonSafeListTest {
    @Test
    public void testUsingGsonSafeArrayList() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonPathTypeAdapterFactory())
                .create();

        InputStream resourceAsStream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream("sample.json");

        GsonSafeList<Integer> typesList = gson.fromJson(
                new InputStreamReader(resourceAsStream),
                new TypeToken<GsonSafeList<Integer>>() {
                }.getType());

        assertEquals(2, typesList.size());
        assertEquals(new Integer(1), typesList.get(0));
        assertEquals(new Integer(4), typesList.get(1));
    }
}
