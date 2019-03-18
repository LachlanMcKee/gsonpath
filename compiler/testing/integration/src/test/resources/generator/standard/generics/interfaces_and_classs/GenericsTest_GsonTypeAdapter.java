package generator.standard.generics.interfaces_and_classs;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class GenericsTest_GsonTypeAdapter extends TypeAdapter<GenericsTest> {
    private final Gson mGson;

    public GenericsTest_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public GenericsTest read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        GenericsTest result = new GenericsTest();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    String value_value1 = mGson.getAdapter(String.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    Map<String, Integer> value_value2 = mGson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    Double value_value3 = mGson.getAdapter(Double.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return result;
    }

    @Override
    public void write(JsonWriter out, GenericsTest value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0);
        }

        Map<String, Integer> obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            mGson.getAdapter(new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}).write(out, obj1);
        }

        Double obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            writeWithGenericAdapter(mGson, obj2.getClass(), out, obj2)
        }

        // End
        out.endObject();
    }
}