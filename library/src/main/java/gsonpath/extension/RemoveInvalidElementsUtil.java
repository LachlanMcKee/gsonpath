package gsonpath.extension;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import gsonpath.GsonPathErrorListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {

    public static <T> void removeInvalidElementsList(
            TypeAdapter<T> adapter,
            JsonReader in,
            List<T> outputList,
            GsonPathErrorListener errorListener) {

        JsonArray jsonArray = Streams.parse(in).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                outputList.add(adapter.fromJsonTree(jsonElement));
            } catch (Exception e) {
                errorListener.onListElementIgnored(e);
            }
        }
    }

    public static <T> List<T> removeInvalidElementsList(
            Class<T> clazz,
            Gson gson,
            GsonPathErrorListener errorListener,
            JsonReader in) throws IOException {

        if (!isValidValue(in)) {
            return null;
        }
        TypeAdapter<T> adapter = gson.getAdapter(clazz);
        List<T> elements = new ArrayList<>();
        removeInvalidElementsList(adapter, in, elements, errorListener);
        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Gson gson,
            GsonPathErrorListener errorListener,
            JsonReader in,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, gson, errorListener, in);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
