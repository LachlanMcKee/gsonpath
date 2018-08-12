package generator.standard;

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
public final class TestExtension_GsonTypeAdapter extends TypeAdapter<TestExtension> {
    private final Gson mGson;

    public TestExtension_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestExtension read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestExtension result = new TestExtension();

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

                    String value_element1 = mGson.getAdapter(String.class).read(in);
                    if (value_element1 != null) {
                        result.element1 = value_element1;
                    }

                    // Gsonpath Extensions
                    if (value_element1 != null) {

                        // Extension - 'EmptyStringToNull' Annotation
                        if (value_element1.trim().length() == 0) {
                            value_element1 = null;
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
    public void write(JsonWriter out, TestExtension value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.element1;
        if (obj0 != null) {
            out.name("element1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}