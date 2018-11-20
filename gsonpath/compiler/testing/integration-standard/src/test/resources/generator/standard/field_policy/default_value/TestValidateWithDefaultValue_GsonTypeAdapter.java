package generator.standard.field_policy.validate_explicit_non_null;

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
public final class TestValidateWithDefaultValue_GsonTypeAdapter extends TypeAdapter<TestValidateWithDefaultValue> {
    private final Gson mGson;

    public TestValidateWithDefaultValue_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestValidateWithDefaultValue read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestValidateWithDefaultValue result = new TestValidateWithDefaultValue();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "mandatoryWithDefault":
                    jsonFieldCounter0++;

                    Integer value_mandatoryWithDefault = mGson.getAdapter(Integer.class).read(in);
                    if (value_mandatoryWithDefault != null) {
                        result.mandatoryWithDefault = value_mandatoryWithDefault;
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
    public void write(JsonWriter out, TestValidateWithDefaultValue value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Integer obj0 = value.mandatoryWithDefault;
        if (obj0 != null) {
            out.name("mandatoryWithDefault");
            mGson.getAdapter(Integer.class).write(out, obj0);
        }

        // End
        out.endObject();
    }
}