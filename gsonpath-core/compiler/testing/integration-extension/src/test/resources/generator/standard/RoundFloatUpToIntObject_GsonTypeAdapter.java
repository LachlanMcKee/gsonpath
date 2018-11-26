package generator.standard;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class RoundFloatUpToIntObject_GsonTypeAdapter extends TypeAdapter<RoundFloatUpToIntObject> {
    private final Gson mGson;

    public RoundFloatUpToIntObject_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public RoundFloatUpToIntObject read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        RoundFloatUpToIntObject result = new RoundFloatUpToIntObject();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "element1":
                    jsonFieldCounter0++;

                    // Extension (Read) - 'RoundFloatUpToInt' Annotation
                    Float value_element1_float = mGson.getAdapter(Float.class).read(in);
                    Integer value_element1;
                    if (value_element1_float != null) {
                        value_element1 = (int) Math.ceil(value_element1_float);
                    } else {
                        value_element1 = null;
                    }

                    if (value_element1 != null) {
                        result.element1 = value_element1;
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
    public void write(JsonWriter out, RoundFloatUpToIntObject value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.element1;
        out.name("element1");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}