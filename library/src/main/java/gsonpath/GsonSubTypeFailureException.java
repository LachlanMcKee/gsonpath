package gsonpath;

import com.google.gson.JsonParseException;

/**
 * An exception which is fired when a {@link GsonSubtype} annotated field using the
 * {@link GsonSubtype#subTypeFailureOutcome()} of {@link GsonSubTypeFailureOutcome#FAIL} fails to correctly
 * deserialize a Json Object.
 */
public class GsonSubTypeFailureException extends JsonParseException {
    public GsonSubTypeFailureException(String message) {
        super(message);
    }
}
