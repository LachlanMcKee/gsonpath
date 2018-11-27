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
public final class TestMutableSize_GsonTypeAdapter extends TypeAdapter<TestMutableSize> {
    private final Gson mGson;

    public TestMutableSize_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestMutableSize read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestMutableSize result = new TestMutableSize();

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

                    String[] value_value1 = mGson.getAdapter(String[].class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }

                    // Gsonpath Extensions
                    if (result.value1 != null) {

                        // Extension - 'Size' Annotation
                        if (result.value1.length < 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected minimum: '0', actual minimum: '" + result.value1.length + "'");
                        }
                        if (result.value1.length > 6) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. Expected maximum: '6', actual maximum: '" + result.value1.length + "'");
                        }
                        if (result.value1.length % 2 != 0) {
                            throw new com.google.gson.JsonParseException("Invalid array length for JSON element 'value1'. length of '" + result.value1.length + "' is not a multiple of 2");
                        }

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
    public void write(JsonWriter out, TestMutableSize value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(String[].class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}