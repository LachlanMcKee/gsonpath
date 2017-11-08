package generator.interf.flatten_json;

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
public final class TestFlattenJsonWithInterface_GsonTypeAdapter extends TypeAdapter<TestFlattenJsonWithInterface> {
    private final Gson mGson;

    public TestFlattenJsonWithInterface_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestFlattenJsonWithInterface read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        
        java.lang.String value_flattenExample = null;

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "flattenExample":
                    jsonFieldCounter0++;

                    JsonElement value_flattenExample_safe = mGson.getAdapter(JsonElement.class).read(in);
                    if (value_flattenExample_safe != null) {
                        value_flattenExample = value_flattenExample_safe.toString();
                    }
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return new TestFlattenJsonWithInterface_GsonPathModel(
                value_flattenExample
        );
    }

    @Override
    public void write(JsonWriter out, TestFlattenJsonWithInterface value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.getFlattenExample();
        if (obj0 != null) {
            out.name("flattenExample");
            out.value(obj0);
        }

        // End
        out.endObject();
    }
}