package generator.standard.empty.invalid_fields;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Override;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestInvalidFields_GsonTypeAdapter extends TypeAdapter<TestInvalidFields> {
    private final Gson mGson;

    public TestInvalidFields_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestInvalidFields read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestInvalidFields result = new TestInvalidFields();

        return result;
    }

    @Override
    public void write(JsonWriter out, TestInvalidFields value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        // End
        out.endObject();
    }
}