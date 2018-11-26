package generator.standard.flatten.valid.nullable;

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
public final class TestImmutableFlattenJson_GsonTypeAdapter extends TypeAdapter<TestImmutableFlattenJson> {
    private final Gson mGson;

    public TestImmutableFlattenJson_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestImmutableFlattenJson read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        String value_value1 = null;

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

                    // Extension (Read) - 'FlattenJson' Annotation
                    JsonElement value_value1_jsonElement = mGson.getAdapter(JsonElement.class).read(in);
                    if (value_value1_jsonElement != null) {
                        value_value1 = value_value1_jsonElement.toString();
                    }

                    break;

                default:
                    in.skipValue();
                    break;

            }
        }

        in.endObject();
        return new TestImmutableFlattenJson(
                value_value1);
    }

    @Override
    public void write(JsonWriter out, TestImmutableFlattenJson value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}