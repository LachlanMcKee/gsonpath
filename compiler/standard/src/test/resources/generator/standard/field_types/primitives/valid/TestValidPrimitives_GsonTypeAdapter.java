package generator.standard.field_types.primitives.valid;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathErrorListener;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
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

@GsonPathGenerated
public final class TestValidPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidPrimitives> {
    public TestValidPrimitives_GsonTypeAdapter(Gson gson, GsonPathErrorListener errorListener) {
        super(gson, errorListener);
    }

    @Override
    public TestValidPrimitives readImpl(JsonReader in) throws IOException {
        TestValidPrimitives result = new TestValidPrimitives();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 8)) {
            switch (in.nextName()) {
                case "value1":
                    Boolean value_value1 = gson.getAdapter(Boolean.class).read(in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Integer value_value2 = gson.getAdapter(Integer.class).read(in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Double value_value3 = gson.getAdapter(Double.class).read(in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    Long value_value4 = gson.getAdapter(Long.class).read(in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Byte value_value5 = gson.getAdapter(Byte.class).read(in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Short value_value6 = gson.getAdapter(Short.class).read(in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Float value_value7 = gson.getAdapter(Float.class).read(in);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    Character value_value8 = gson.getAdapter(Character.class).read(in);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestValidPrimitives value) throws IOException {
        // Begin
        out.beginObject();
        boolean obj0 = value.value1;
        out.name("value1");
        gson.getAdapter(Boolean.class).write(out, obj0);

        int obj1 = value.value2;
        out.name("value2");
        gson.getAdapter(Integer.class).write(out, obj1);

        double obj2 = value.value3;
        out.name("value3");
        gson.getAdapter(Double.class).write(out, obj2);

        long obj3 = value.value4;
        out.name("value4");
        gson.getAdapter(Long.class).write(out, obj3);

        byte obj4 = value.value5;
        out.name("value5");
        gson.getAdapter(Byte.class).write(out, obj4);

        short obj5 = value.value6;
        out.name("value6");
        gson.getAdapter(Short.class).write(out, obj5);

        float obj6 = value.value7;
        out.name("value7");
        gson.getAdapter(Float.class).write(out, obj6);

        char obj7 = value.value8;
        out.name("value8");
        gson.getAdapter(Character.class).write(out, obj7);

        // End
        out.endObject();
    }
}