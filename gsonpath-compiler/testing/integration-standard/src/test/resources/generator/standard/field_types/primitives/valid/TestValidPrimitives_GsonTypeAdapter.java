package generator.standard.field_types.primitives.valid;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Boolean;
import java.lang.Byte;
import java.lang.Character;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.Short;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestValidPrimitives_GsonTypeAdapter extends TypeAdapter<TestValidPrimitives> {
    private final Gson mGson;

    public TestValidPrimitives_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestValidPrimitives read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestValidPrimitives result = new TestValidPrimitives();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 8) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    Boolean value_value1 = mGson.getAdapter(Boolean.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    Integer value_value2 = mGson.getAdapter(Integer.class).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    Double value_value3 = mGson.getAdapter(Double.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    jsonFieldCounter0++;

                    Long value_value4 = mGson.getAdapter(Long.class).read(in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    jsonFieldCounter0++;

                    Byte value_value5 = mGson.getAdapter(Byte.class).read(in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    jsonFieldCounter0++;

                    Short value_value6 = mGson.getAdapter(Short.class).read(in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    jsonFieldCounter0++;

                    Float value_value7 = mGson.getAdapter(Float.class).read(in);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    jsonFieldCounter0++;

                    Character value_value8 = mGson.getAdapter(Character.class).read(in);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
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
    public void write(JsonWriter out, TestValidPrimitives value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        boolean obj0 = value.value1;
        out.name("value1");
        mGson.getAdapter(Boolean.class).write(out, obj0);

        int obj1 = value.value2;
        out.name("value2");
        mGson.getAdapter(Integer.class).write(out, obj1);

        double obj2 = value.value3;
        out.name("value3");
        mGson.getAdapter(Double.class).write(out, obj2);

        long obj3 = value.value4;
        out.name("value4");
        mGson.getAdapter(Long.class).write(out, obj3);

        byte obj4 = value.value5;
        out.name("value5");
        mGson.getAdapter(Byte.class).write(out, obj4);

        short obj5 = value.value6;
        out.name("value6");
        mGson.getAdapter(Short.class).write(out, obj5);

        float obj6 = value.value7;
        out.name("value7");
        mGson.getAdapter(Float.class).write(out, obj6);

        char obj7 = value.value8;
        out.name("value8");
        mGson.getAdapter(Character.class).write(out, obj7);

        // End
        out.endObject();
    }
}