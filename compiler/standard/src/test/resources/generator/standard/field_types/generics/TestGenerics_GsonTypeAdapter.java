package generator.standard.field_types.generics;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TestGenerics_GsonTypeAdapter extends GsonPathTypeAdapter<TestGenerics> {
    public TestGenerics_GsonTypeAdapter(GsonPathTypeAdapter.Arguments arguments) {
        super(arguments);
    }

    @Override
    public TestGenerics readImpl(JsonReader in) throws IOException {
        TestGenerics result = new TestGenerics();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    List<String> value_value1 = gson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
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
    public void writeImpl(JsonWriter out, TestGenerics value) throws IOException {
        // Begin
        out.beginObject();
        List<String> obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            gson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj0);
        }

        // End
        out.endObject();
    }
}