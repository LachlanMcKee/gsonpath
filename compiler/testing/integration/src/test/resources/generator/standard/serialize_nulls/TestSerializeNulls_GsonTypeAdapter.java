package generator.standard.class_annotations.serialize_nulls;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestSerializeNulls_GsonTypeAdapter extends TypeAdapter<TestSerializeNulls> {
    private final Gson mGson;

    public TestSerializeNulls_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestSerializeNulls read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestSerializeNulls result = new TestSerializeNulls();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 7) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    Integer value_value1 = mGson.getAdapter(Integer.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    Double value_value2 = mGson.getAdapter(Double.class).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    Boolean value_value3 = mGson.getAdapter(Boolean.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    jsonFieldCounter0++;

                    String value_value4 = mGson.getAdapter(String.class).read(in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    jsonFieldCounter0++;

                    Integer value_value5 = mGson.getAdapter(Integer.class).read(in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    jsonFieldCounter0++;

                    Double value_value6 = mGson.getAdapter(Double.class).read(in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    jsonFieldCounter0++;

                    Boolean value_value7 = mGson.getAdapter(Boolean.class).read(in);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
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
    public void write(JsonWriter out, TestSerializeNulls value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("value1");
        mGson.getAdapter(Integer.class).write(out, obj0);

        double obj1 = value.value2;
        out.name("value2");
        mGson.getAdapter(Double.class).write(out, obj1);

        boolean obj2 = value.value3;
        out.name("value3");
        mGson.getAdapter(Boolean.class).write(out, obj2);

        String obj3 = value.value4;
        out.name("value4");
        if (obj3 != null) {
            writeWithGenericAdapter(mGson, obj3.getClass(), out, obj3)
        } else {
            out.nullValue();
        }

        Integer obj4 = value.value5;
        out.name("value5");
        if (obj4 != null) {
            writeWithGenericAdapter(mGson, obj4.getClass(), out, obj4)
        } else {
            out.nullValue();
        }

        Double obj5 = value.value6;
        out.name("value6");
        if (obj5 != null) {
            writeWithGenericAdapter(mGson, obj5.getClass(), out, obj5)
        } else {
            out.nullValue();
        }

        Boolean obj6 = value.value7;
        out.name("value7");
        if (obj6 != null) {
            writeWithGenericAdapter(mGson, obj6.getClass(), out, obj6)
        } else {
            out.nullValue();
        }

        // End
        out.endObject();
    }
}