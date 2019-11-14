package gsonpath;

import com.google.gson.JsonParseException;

public abstract class JsonFieldMissingException extends JsonParseException {
    public JsonFieldMissingException(String message) {
        super(message);
    }
}
