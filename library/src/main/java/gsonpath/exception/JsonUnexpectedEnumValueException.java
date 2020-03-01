package gsonpath.exception;

import com.google.gson.JsonParseException;

public class JsonUnexpectedEnumValueException extends JsonParseException {
    public JsonUnexpectedEnumValueException(String value, String className) {
        super("Unexpected enum value '" + value + "' for class '" + className + "'");
    }
}
