package generator.standard.invalid.immutable;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.extension.RemoveInvalidElementsUtil;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestImmutableRemoveInvalidElements_GsonTypeAdapter extends TypeAdapter<TestImmutableRemoveInvalidElements> {
    private final Gson mGson;

    public TestImmutableRemoveInvalidElements_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestImmutableRemoveInvalidElements read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        String[] value_value1 = null;
        List<String> value_value2 = null;

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, mGson, in);

                    break;

                case "value2":
                    jsonFieldCounter0++;

                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, mGson, in);

                    break;

                default:
                    in.skipValue();
                    break;

            }
        }

        in.endObject();
        return new TestImmutableRemoveInvalidElements(
                value_value1,
                value_value2);
    }

    @Override
    public void write(JsonWriter out, TestImmutableRemoveInvalidElements value) throws IOException {
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

        List<String> obj1 = value.getValue2();
        if (obj1 != null) {
            out.name("value2");
            mGson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj1);
        }

        // End
        out.endObject();
    }
}