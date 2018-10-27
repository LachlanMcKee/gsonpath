package generator.standard.field_annotations.exclude;

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
public final class TestExclude_GsonTypeAdapter extends TypeAdapter<TestExclude> {
    private final Gson mGson;

    public TestExclude_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestExclude read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestExclude result = new TestExclude();

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

                    Integer value_element1 = mGson.getAdapter(Integer.class).read(in);
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
    public void write(JsonWriter out, TestExclude value) throws IOException {
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