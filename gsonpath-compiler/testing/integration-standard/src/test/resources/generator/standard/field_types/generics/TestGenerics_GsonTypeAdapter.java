package generator.standard.field_types.generics;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestGenerics_GsonTypeAdapter extends TypeAdapter<TestGenerics> {
    private final Gson mGson;

    public TestGenerics_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestGenerics read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestGenerics result = new TestGenerics();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    List<String> value_value1 = mGson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>() {
                    }).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
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
    public void write(JsonWriter out, TestGenerics value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        List<String> obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj0);
        }

        // End
        out.endObject();
    }
}