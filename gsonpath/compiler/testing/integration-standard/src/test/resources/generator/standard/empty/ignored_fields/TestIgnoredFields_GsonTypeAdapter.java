package generator.standard.empty.ignored_fields;

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
public final class TestIgnoredFields_GsonTypeAdapter extends TypeAdapter<TestIgnoredFields> {
    private final Gson mGson;

    public TestIgnoredFields_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestIgnoredFields read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestIgnoredFields result = new TestIgnoredFields();

        return result;
    }

    @Override
    public void write(JsonWriter out, TestIgnoredFields value) throws IOException {
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