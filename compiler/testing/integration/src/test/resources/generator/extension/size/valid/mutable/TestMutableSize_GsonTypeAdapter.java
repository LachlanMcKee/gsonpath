package generator.standard.size.valid.nullable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GeneratedAdapter(adapterElementClassNames = {"generator.standard.size.valid.nullable.TestMutableSize"})
public final class TestMutableSize_GsonTypeAdapter extends GsonPathTypeAdapter<TestMutableSize> {
    public TestMutableSize_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestMutableSize readImpl(JsonReader in) throws IOException {
        TestMutableSize result = new TestMutableSize();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    String[] value_value1 = gson.getAdapter(String[].class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }

                    // Gsonpath Extensions
                    if (result.value1 != null) {

                        // Extension - 'Size' Annotation
                        if (result.value1.length != 1) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected length: '1', actual length: '" + result.value1.length + "'");
                        }

                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestMutableSize value) throws IOException {
        // Begin
        out.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}