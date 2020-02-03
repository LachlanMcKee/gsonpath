package generator.standard.field_annotations.exclude;

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
public final class TestExclude_GsonTypeAdapter extends GsonPathTypeAdapter<TestExclude> {
    public TestExclude_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public TestExclude readImpl(JsonReader in) throws IOException {
        TestExclude result = new TestExclude();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "element1":
                    Integer value_element1 = gson.getAdapter(Integer.class).read(in);
                    if (value_element1 != null) {
                        result.element1 = value_element1;
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
    public void writeImpl(JsonWriter out, TestExclude value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.element1;
        out.name("element1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}