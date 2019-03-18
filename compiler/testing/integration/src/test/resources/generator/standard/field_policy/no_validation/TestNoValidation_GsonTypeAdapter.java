package generator.standard.field_policy.no_validation;

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
public final class TestNoValidation_GsonTypeAdapter extends TypeAdapter<TestNoValidation> {
    private final Gson mGson;

    public TestNoValidation_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestNoValidation read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestNoValidation result = new TestNoValidation();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "optional1":
                    jsonFieldCounter0++;

                    Integer value_optional1 = mGson.getAdapter(Integer.class).read(in);
                    if (value_optional1 != null) {
                        result.optional1 = value_optional1;
                    }
                    break;

                case "optional2":
                    jsonFieldCounter0++;

                    Integer value_optional2 = mGson.getAdapter(Integer.class).read(in);
                    if (value_optional2 != null) {
                        result.optional2 = value_optional2;
                    }
                    break;

                case "optional3":
                    jsonFieldCounter0++;

                    Integer value_optional3 = mGson.getAdapter(Integer.class).read(in);
                    if (value_optional3 != null) {
                        result.optional3 = value_optional3;
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
    public void write(JsonWriter out, TestNoValidation value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Integer obj0 = value.optional1;
        if (obj0 != null) {
            out.name("optional1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0)
        }

        Integer obj1 = value.optional2;
        if (obj1 != null) {
            out.name("optional2");
            writeWithGenericAdapter(mGson, obj1.getClass(), out, obj1)
        }

        int obj2 = value.optional3;
        out.name("optional3");
        mGson.getAdapter(Integer.class).write(out, obj2);

        // End
        out.endObject();
    }
}