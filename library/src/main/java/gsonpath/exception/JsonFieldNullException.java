package gsonpath.exception;

public class JsonFieldNullException extends JsonFieldMissingException {
    public JsonFieldNullException(String field, String className) {
        super("Mandatory JSON element '" + field + "' was null for class '" + className + "'");
    }
}
