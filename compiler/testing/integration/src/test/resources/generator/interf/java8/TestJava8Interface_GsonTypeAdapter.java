package generator.interf.java8;

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
public final class TestJava8Interface_GsonTypeAdapter extends TypeAdapter<TestJava8Interface> {
    private final Gson mGson;

    public TestJava8Interface_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestJava8Interface read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        Integer value_value1 = null;

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

                    value_value1 = mGson.getAdapter(Integer.class).read(in);
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return new TestJava8Interface_GsonPathModel(
                value_value1
        );
    }

    @Override
    public void write(JsonWriter out, TestJava8Interface value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Integer obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}