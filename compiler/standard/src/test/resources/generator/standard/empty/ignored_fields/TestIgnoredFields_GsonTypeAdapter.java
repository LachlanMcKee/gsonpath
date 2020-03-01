package generator.standard.empty.ignored_fields;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.JsonReaderHelper;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestIgnoredFields_GsonTypeAdapter extends GsonPathTypeAdapter<TestIgnoredFields> {
    public TestIgnoredFields_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestIgnoredFields readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
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