package gsonpath;

import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class GsonErrors {
    private List<JsonParseException> errors;

    public void addError(JsonParseException error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public List<JsonParseException> getErrors() {
        return errors;
    }
}
