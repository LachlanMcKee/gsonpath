package generator.standard.size.valid.nullable;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
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
public final class TestImmutableSize_GsonTypeAdapter extends TypeAdapter<TestImmutableSize> {
    private final Gson mGson;

    public TestImmutableSize_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestImmutableSize read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        String[] value_value1 = null;

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

                    value_value1 = mGson.getAdapter(String[].class).read(in);

                    // Gsonpath Extensions
                    if (value_value1 != null) {

                        // Extension - 'Size' Annotation
                        if (value_value1.length < 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected minimum: '0', actual minimum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length > 6) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected maximum: '6', actual maximum: '" + value_value1.length + "'");
                        }
                        if (value_value1.length % 2 != 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. length of '" + value_value1.length + "' is not a multiple of 2");
                        }

                    }
                    break;

                default:
                    in.skipValue();
                    break;

            }
        }

        in.endObject();
        return new TestImmutableSize(
                value_value1);
    }

    @Override
    public void write(JsonWriter out, TestImmutableSize value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String[] obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(String[].class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}