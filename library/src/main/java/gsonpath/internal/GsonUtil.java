package gsonpath.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A set of Gson utilities which expose functionality to read content from a JsonReader
 * safely without throwing IOExceptions.
 */
public class GsonUtil {

    private GsonUtil() {
    }

    /**
     * Determines whether the next value within the reader is not null.
     *
     * @param in the json reader used to read the stream
     * @return true if a valid value exists, or false if the value is null.
     * @throws IOException see {@link JsonReader#skipValue()}
     */
    public static boolean isValidValue(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.skipValue();
            return false;
        }
        return true;
    }

    public static <T> T read(Gson gson, Class<T> type, GsonErrors gsonErrors, JsonReader in) throws IOException {
        return read(gson, TypeToken.get(type), gsonErrors, in);
    }

    public static <T> T read(Gson gson, TypeToken<T> type, GsonErrors gsonErrors, JsonReader in) throws IOException {
        TypeAdapter<T> adapter = gson.getAdapter(type);
        if (gsonErrors != null) {
            if (adapter instanceof GsonPathTypeAdapter) {
                return ((GsonPathTypeAdapter<T>) adapter).readImpl(in, gsonErrors);
            } else {
                return adapter.read(in);
            }
        } else {
            return adapter.read(in);
        }
    }

    public static <T> T fromJsonTree(Gson gson, Class<T> type, GsonErrors gsonErrors, JsonElement jsonTree) {
        return fromJsonTree(gson, TypeToken.get(type), gsonErrors, jsonTree);
    }

    public static <T> T fromJsonTree(Gson gson, TypeToken<T> type, GsonErrors gsonErrors, JsonElement jsonTree) {
        try {
            JsonReader jsonReader = new JsonTreeReader(jsonTree);
            return read(gson, type, gsonErrors, jsonReader);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    public static <T> void writeWithGenericAdapter(Gson gson, Class<? extends T> clazz, JsonWriter out, T obj0) throws IOException {
        TypeAdapter adapter = gson.getAdapter(clazz);
        adapter.write(out, obj0);
    }
}
