package generator.standard.field_types.boxed_primitives;

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
import java.lang.String;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestBoxedPrimitives_GsonTypeAdapter extends TypeAdapter<TestBoxedPrimitives> {
    private final Gson mGson;

    public TestBoxedPrimitives_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestBoxedPrimitives read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestBoxedPrimitives result = new TestBoxedPrimitives();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 9) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    String value_value1 = mGson.getAdapter(String.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    Boolean value_value2 = mGson.getAdapter(Boolean.class).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    Integer value_value3 = mGson.getAdapter(Integer.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    jsonFieldCounter0++;

                    Double value_value4 = mGson.getAdapter(Double.class).read(in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    jsonFieldCounter0++;

                    Long value_value5 = mGson.getAdapter(Long.class).read(in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    jsonFieldCounter0++;

                    Byte value_value6 = mGson.getAdapter(Byte.class).read(in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    jsonFieldCounter0++;

                    Short value_value7 = mGson.getAdapter(Short.class).read(in);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    jsonFieldCounter0++;

                    Float value_value8 = mGson.getAdapter(Float.class).read(in);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
                    }
                    break;

                case "value9":
                    jsonFieldCounter0++;

                    Character value_value9 = mGson.getAdapter(Character.class).read(in);
                    if (value_value9 != null) {
                        result.value9 = value_value9;
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
    public void write(JsonWriter out, TestBoxedPrimitives value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0);
        }

        Boolean obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            writeWithGenericAdapter(mGson, obj1.getClass(), out, obj1)
        }

        Integer obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            writeWithGenericAdapter(mGson, obj2.getClass(), out, obj2)
        }

        Double obj3 = value.value4;
        if (obj3 != null) {
            out.name("value4");
            writeWithGenericAdapter(mGson, obj3.getClass(), out, obj3)
        }

        Long obj4 = value.value5;
        if (obj4 != null) {
            out.name("value5");
            writeWithGenericAdapter(mGson, obj4.getClass(), out, obj4)
        }

        Byte obj5 = value.value6;
        if (obj5 != null) {
            out.name("value6");
            writeWithGenericAdapter(mGson, obj5.getClass(), out, obj5)
        }

        Short obj6 = value.value7;
        if (obj6 != null) {
            out.name("value7");
            writeWithGenericAdapter(mGson, obj6.getClass(), out, obj6)
        }

        Float obj7 = value.value8;
        if (obj7 != null) {
            out.name("value8");
            writeWithGenericAdapter(mGson, obj7.getClass(), out, obj7)
        }

        Character obj8 = value.value9;
        if (obj8 != null) {
            out.name("value9");
            writeWithGenericAdapter(mGson, obj8.getClass(), out, obj8)
        }

        // End
        out.endObject();
    }
}