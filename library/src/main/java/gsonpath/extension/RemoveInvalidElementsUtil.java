package gsonpath.extension;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import gsonpath.GsonErrors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.internal.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {

    public static <T> void removeInvalidElementsList(TypeAdapter<T> adapter, JsonReader in, List<T> outputList, GsonErrors gsonErrors) {
        JsonArray jsonArray = Streams.parse(in).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                outputList.add(adapter.fromJsonTree(jsonElement));
            } catch (JsonParseException e) {
                if (gsonErrors != null) {
                    gsonErrors.addError(e);
                }
            }
        }
    }

    public static <T> List<T> removeInvalidElementsList(Class<T> clazz, Gson gson, JsonReader in, GsonErrors gsonErrors) throws IOException {
        if (!isValidValue(in)) {
            return null;
        }
        TypeAdapter<T> adapter = gson.getAdapter(clazz);
        List<T> elements = new ArrayList<>();
        removeInvalidElementsList(adapter, in, elements, gsonErrors);
        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Gson gson,
            JsonReader in,
            GsonErrors gsonErrors,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, gson, in, gsonErrors);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
