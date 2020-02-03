package gsonpath.extension;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import gsonpath.GsonPathListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {

    public static <T> void removeInvalidElementsList(
            TypeAdapter<T> adapter,
            JsonReader in,
            List<T> outputList,
            GsonPathListener listener) {

        JsonArray jsonArray = Streams.parse(in).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                outputList.add(adapter.fromJsonTree(jsonElement));
            } catch (Exception e) {
                if (listener != null) {
                    listener.onListElementIgnored(e);
                }
            }
        }
    }

    public static <T> List<T> removeInvalidElementsList(
            Class<T> clazz,
            Gson gson,
            GsonPathListener listener,
            JsonReader in) throws IOException {

        if (!isValidValue(in)) {
            return null;
        }
        TypeAdapter<T> adapter = gson.getAdapter(clazz);
        List<T> elements = new ArrayList<>();
        removeInvalidElementsList(adapter, in, elements, listener);
        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Gson gson,
            GsonPathListener listener,
            JsonReader in,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, gson, listener, in);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
