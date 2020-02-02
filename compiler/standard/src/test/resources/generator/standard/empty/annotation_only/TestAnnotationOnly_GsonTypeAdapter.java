package generator.standard.empty.annotation_only;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestAnnotationOnly_GsonTypeAdapter extends GsonPathTypeAdapter<TestAnnotationOnly> {
    public TestAnnotationOnly_GsonTypeAdapter(GsonPathTypeAdapter.Arguments arguments) {
        super(arguments);
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