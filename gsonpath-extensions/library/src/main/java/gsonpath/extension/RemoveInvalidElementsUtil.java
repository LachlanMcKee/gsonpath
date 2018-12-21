package gsonpath.extension;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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

        in.beginArray();
        while (in.hasNext()) {
            try {
                elements.add(gson.getAdapter(clazz).read(in));
            } catch (JsonParseException ignored) {
            }
        }
        in.endArray();

        return elements;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] removeInvalidElementsArray(Class<T> clazz, Gson gson, JsonReader in) throws IOException {
        List<T> adjustedList = removeInvalidElementsList(clazz, gson, in);
        if (adjustedList == null) {
            return null;
        }
        return (T[]) adjustedList.toArray();
    }
}
