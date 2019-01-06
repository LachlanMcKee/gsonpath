package gsonpath.extension;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {
    public static <T> List<T> removeInvalidElementsList(Class<T> clazz, Gson gson, JsonReader in) throws IOException {
        if (!isValidValue(in)) {
            return null;
        }
        List<T> elements = new ArrayList<>();

        TypeAdapter<T> adapter = gson.getAdapter(clazz);
        JsonArray jsonArray = Streams.parse(in).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                elements.add(adapter.fromJsonTree(jsonElement));
            } catch (Exception ignored) {
            }
        }

        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Gson gson,
            JsonReader in,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, gson, in);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
