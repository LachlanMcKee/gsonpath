package generator.standard.empty.invalid_fields;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;

@GsonPathGenerated
public final class TestInvalidFields_GsonTypeAdapter extends GsonPathTypeAdapter<TestInvalidFields> {
    public TestInvalidFields_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestInvalidFields readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestInvalidFields result = new TestInvalidFields();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestInvalidFields value) throws IOException {
        // Begin
        out.beginObject();
        // End
        out.endObject();
    }
}