package generator.standard.empty.ignored_fields;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GeneratedAdapter(adapterElementClassNames = {"generator.standard.empty.ignored_fields.TestIgnoredFields"})
public final class TestIgnoredFields_GsonTypeAdapter extends GsonPathTypeAdapter<TestIgnoredFields> {
    public TestIgnoredFields_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestIgnoredFields readImpl(JsonReader in) throws IOException {
        TestIgnoredFields result = new TestIgnoredFields();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestIgnoredFields value) throws IOException {
        // Begin
        out.beginObject();
        // End
        out.endObject();
    }
}