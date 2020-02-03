package generator.standard.nested_json.field_nesting_autocomplete_inheritance;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestFieldNestingAutocomplete_GsonTypeAdapter extends GsonPathTypeAdapter<TestFieldNestingAutocomplete> {
    public TestFieldNestingAutocomplete_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public TestFieldNestingAutocomplete readImpl(JsonReader in) throws IOException {
        int value_Json1_value1 = 0;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "value1":
                                value_Json1_value1 = gson.getAdapter(Integer.class).read(in);
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestFieldNestingAutocomplete(
                value_Json1_value1);
    }

    @Override
    public void writeImpl(JsonWriter out, TestFieldNestingAutocomplete value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Json1
        out.name("Json1");
        out.beginObject();
        int obj0 = value.getValue1();
        out.name("value1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Json1
        out.endObject();
        // End
        out.endObject();
    }
}