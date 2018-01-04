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

    /**
     * Attempts to obtain the next string value from the reader if possible, otherwise
     * it will return null.
     *
     * @param in the json reader used to read the stream
     * @return the String value if found within the stream, otherwise null.
     * @throws IOException see {#isValidValue()} and {@link JsonReader#nextString()}
     */
    @SuppressWarnings("unused")
    public static String getStringSafely(JsonReader in) throws IOException {
        if (isValidValue(in)) {
            return in.nextString();
        }
        return null;
    }

    /**
     * Attempts to obtain the next boolean value from the reader if possible, otherwise
     * it will return null.
     *
     * @param in the json reader used to read the stream
     * @return the Boolean value if found within the stream, otherwise null.
     * @throws IOException see {#isValidValue()} and {@link JsonReader#nextBoolean()}
     */
    @SuppressWarnings("unused")
    public static Boolean getBooleanSafely(JsonReader in) throws IOException {
        if (isValidValue(in)) {
            return in.nextBoolean();
        }
        return null;
    }

    /**
     * Attempts to obtain the next integer value from the reader if possible, otherwise
     * it will return null.
     *
     * @param in the json reader used to read the stream
     * @return the Integer value if found within the stream, otherwise null.
     * @throws IOException see {#isValidValue()} and {@link JsonReader#nextInt()}
     */
    @SuppressWarnings("unused")
    public static Integer getIntegerSafely(JsonReader in) throws IOException {
        if (isValidValue(in)) {
            try {
                return in.nextInt();
            } catch (NumberFormatException ignored) {
                // Fall back to return null
                in.skipValue();
            }
        }
        return null;
    }

    /**
     * Attempts to obtain the next long value from the reader if possible, otherwise
     * it will return null.
     *
     * @param in the json reader used to read the stream
     * @return the Long value if found within the stream, otherwise null.
     * @throws IOException see {#isValidValue()} and {@link JsonReader#nextLong()}
     */
    @SuppressWarnings("unused")
    public static Long getLongSafely(JsonReader in) throws IOException {
        if (isValidValue(in)) {
            try {
                return in.nextLong();
            } catch (NumberFormatException ignored) {
                // Fall back to return null
                in.skipValue();
            }
        }
        return null;
    }

    /**
     * Attempts to obtain the next double value from the reader if possible, otherwise
     * it will return null.
     *
     * @param in the json reader used to read the stream
     * @return the Double value if found within the stream, otherwise null.
     * @throws IOException see {#isValidValue()} and {@link JsonReader#nextDouble()}
     */
    @SuppressWarnings("unused")
    public static Double getDoubleSafely(JsonReader in) throws IOException {
        if (isValidValue(in)) {
            try {
                return in.nextDouble();
            } catch (NumberFormatException ignored) {
                // Fall back to return null
                in.skipValue();
            }
        }
        return null;
    }

}
