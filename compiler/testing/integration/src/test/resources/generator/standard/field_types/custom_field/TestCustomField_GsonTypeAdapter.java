package generator.standard.field_types.custom_field;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Override;
import java.util.Currency;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestCustomField_GsonTypeAdapter extends TypeAdapter<TestCustomField> {
    private final Gson mGson;

    public TestCustomField_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestCustomField read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestCustomField result = new TestCustomField();

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

                    Currency value_value1 = mGson.getAdapter(Currency.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
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
    public void write(JsonWriter out, TestCustomField value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Currency obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0)
        }

        // End
        out.endObject();
    }
}