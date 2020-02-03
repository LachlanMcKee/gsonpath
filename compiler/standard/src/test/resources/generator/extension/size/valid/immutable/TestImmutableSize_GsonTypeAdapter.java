package generator.standard.size.valid.nullable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestImmutableSize_GsonTypeAdapter extends GsonPathTypeAdapter<TestImmutableSize> {
    public TestImmutableSize_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public TestImmutableSize readImpl(JsonReader in) throws IOException {
        String[] value_value1 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    value_value1 = gson.getAdapter(String[].class).read(in);

                    // Gsonpath Extensions
                    if (value_value1 != null) {

                        // Extension - 'Size' Annotation
                        if (value_value1.length < 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected minimum: '0', actual minimum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length > 6) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected maximum: '6', actual maximum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length % 2 != 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. length of '" + value_value1.length + "' is not a multiple of 2");
                        }

                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestImmutableSize(
                value_value1);
    }

    @Override
    public void writeImpl(JsonWriter out, TestImmutableSize value) throws IOException {
        // Begin
        out.beginObject();
        String[] obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}