package generator.standard.generics.interfaces;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

@GsonPathGenerated
public final class GenericsTest_GsonTypeAdapter extends GsonPathTypeAdapter<GenericsTest> {
    public GenericsTest_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public GenericsTest readImpl(JsonReader in) throws IOException {
        String value_value1 = null;
        Map<String, Integer> value_value2 = null;
        Double value_value3 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "value1":
                    value_value1 = gson.getAdapter(String.class).read(in);
                    break;

                case "value2":
                    value_value2 = gson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).read(in);
                    break;

                case "value3":
                    value_value3 = gson.getAdapter(Double.class).read(in);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new GenericsTest_GsonPathModel(
                value_value1,
                value_value2,
                value_value3);
    }

    @Override
    public void writeImpl(JsonWriter out, GenericsTest value) throws IOException {
        // Begin
        out.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Map<String, Integer> obj1 = value.getValue2();
        if (obj1 != null) {
            out.name("value2");
            gson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).write(out, obj1);
        }

        Double obj2 = value.getValue3();
        if (obj2 != null) {
            out.name("value3");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End
        out.endObject();
    }
}