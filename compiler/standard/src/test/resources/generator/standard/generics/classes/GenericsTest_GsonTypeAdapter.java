package generator.standard.generics.classes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;

import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

@GsonPathGenerated
public final class GenericsTest_GsonTypeAdapter extends GsonPathTypeAdapter<GenericsTest> {
    public GenericsTest_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public GenericsTest readImpl(JsonReader in) throws IOException {
        GenericsTest result = new GenericsTest();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "value1":
                    String value_value1 = gson.getAdapter(String.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Map<String, Integer> value_value2 = gson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Double value_value3 = gson.getAdapter(Double.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
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
    public void writeImpl(JsonWriter out, GenericsTest value) throws IOException {
        // Begin
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Map<String, Integer> obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            gson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).write(out, obj1);
        }

        Double obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End
        out.endObject();
    }
}