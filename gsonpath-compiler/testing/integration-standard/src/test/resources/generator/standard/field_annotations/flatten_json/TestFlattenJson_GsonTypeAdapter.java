package generator.standard.field_annotations.flatten_json;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Override;
import java.lang.String;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestFlattenJson_GsonTypeAdapter extends TypeAdapter<TestFlattenJson> {
    private final Gson mGson;

    public TestFlattenJson_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestFlattenJson read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestFlattenJson result = new TestFlattenJson();

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

                    JsonElement value_value1 = mGson.getAdapter(JsonElement.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1.toString();
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
    public void write(JsonWriter out, TestFlattenJson value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}