package generator.standard.empty.annotation_only;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestAnnotationOnly_GsonTypeAdapter extends GsonPathTypeAdapter<TestAnnotationOnly> {
    public TestAnnotationOnly_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestAnnotationOnly readImpl(JsonReader in) throws IOException {
        TestAnnotationOnly result = new TestAnnotationOnly();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestAnnotationOnly value) throws IOException {
        // Begin
        out.beginObject();
        // End
        out.endObject();
    }
}