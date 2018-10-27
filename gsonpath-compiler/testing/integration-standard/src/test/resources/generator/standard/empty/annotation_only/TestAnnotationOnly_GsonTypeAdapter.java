package generator.standard.empty.annotation_only;

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
public final class TestAnnotationOnly_GsonTypeAdapter extends TypeAdapter<TestAnnotationOnly> {
    private final Gson mGson;

    public TestAnnotationOnly_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestAnnotationOnly read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestAnnotationOnly result = new TestAnnotationOnly();

        return result;
    }

    @Override
    public void write(JsonWriter out, TestAnnotationOnly value) throws IOException {
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