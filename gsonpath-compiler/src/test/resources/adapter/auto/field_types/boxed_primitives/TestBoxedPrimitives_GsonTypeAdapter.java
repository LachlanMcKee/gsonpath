package adapter.auto.field_types.boxed_primitives;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;

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
            if (jsonFieldCounter0 == 5) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value1":
                    jsonFieldCounter0++;

                    String safeValue0 = getStringSafely(in);
                    if (safeValue0 != null) {
                        result.value1 = safeValue0;
                    }
                    break;

                case "value2":
                    jsonFieldCounter0++;

                    Boolean safeValue1 = getBooleanSafely(in);
                    if (safeValue1 != null) {
                        result.value2 = safeValue1;
                    }
                    break;

                case "value3":
                    jsonFieldCounter0++;

                    Integer safeValue2 = getIntegerSafely(in);
                    if (safeValue2 != null) {
                        result.value3 = safeValue2;
                    }
                    break;

                case "value4":
                    jsonFieldCounter0++;

                    Double safeValue3 = getDoubleSafely(in);
                    if (safeValue3 != null) {
                        result.value4 = safeValue3;
                    }
                    break;

                case "value5":
                    jsonFieldCounter0++;

                    Long safeValue4 = getLongSafely(in);
                    if (safeValue4 != null) {
                        result.value5 = safeValue4;
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
            out.value(obj0);
        }

        Boolean obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            out.value(obj1);
        }

        Integer obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            out.value(obj2);
        }

        Double obj3 = value.value4;
        if (obj3 != null) {
            out.name("value4");
            out.value(obj3);
        }

        Long obj4 = value.value5;
        if (obj4 != null) {
            out.name("value5");
            out.value(obj4);
        }

        // End
        out.endObject();
    }
}