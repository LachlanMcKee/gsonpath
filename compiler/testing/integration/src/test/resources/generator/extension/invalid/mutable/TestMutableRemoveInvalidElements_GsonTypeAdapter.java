package generator.standard.invalid.mutable;

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
public final class TestMutableRemoveInvalidElements_GsonTypeAdapter extends TypeAdapter<TestMutableRemoveInvalidElements> {
    private final Gson mGson;

    public TestMutableRemoveInvalidElements_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestMutableRemoveInvalidElements read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestMutableRemoveInvalidElements result = new TestMutableRemoveInvalidElements();

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
                    String[] value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, mGson, in, new RemoveInvalidElementsUtil.CreateArrayFunction<String>() {
                        @Override
                        public String[] createArray() {
                            return new String[0];
                        }
                    });

                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    List<String> value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, mGson, in);

                    if (value_value2 != null) {
                        result.value2 = value_value2;
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
    public void write(JsonWriter out, TestMutableRemoveInvalidElements value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0)
        }

        List<String> obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            mGson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj1);
        }

        // End
        out.endObject();
    }
}