package gsonpath.exception;

public class JsonFieldNoKeyException extends JsonFieldMissingException {
    public JsonFieldNoKeyException(String field, String className) {
        super("Mandatory JSON element '" + field + "' was not found within class '" + className + "'");
    }
}
