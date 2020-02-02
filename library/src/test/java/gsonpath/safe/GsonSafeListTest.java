package gsonpath.safe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import gsonpath.GsonPathErrorListener;
import gsonpath.GsonPathTypeAdapterFactory;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GsonSafeListTest {
    @Captor
    ArgumentCaptor<Exception> exceptionArgumentCaptor;

    @Test
    public void testUsingGsonSafeArrayList() {
        MockitoAnnotations.initMocks(this);

        GsonPathErrorListener errorListener = mock(GsonPathErrorListener.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonPathTypeAdapterFactory(errorListener))
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

        verify(errorListener, times(2)).onListElementIgnored(exceptionArgumentCaptor.capture());
        Exception exception1 = exceptionArgumentCaptor.getAllValues().get(0);
        assertEquals(JsonSyntaxException.class, exception1.getClass());
        assertEquals("java.lang.NumberFormatException: For input string: \"a\"", exception1.getMessage());

        Exception exception2 = exceptionArgumentCaptor.getAllValues().get(1);
        assertEquals(JsonSyntaxException.class, exception2.getClass());
        assertEquals("java.lang.NumberFormatException: For input string: \"b\"", exception2.getMessage());
    }
}
