package gsonpath;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

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
}
