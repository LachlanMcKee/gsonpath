package generator.standard.field_types.boxed_primitives;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;

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

@GsonPathGenerated
public final class TestBoxedPrimitives_GsonTypeAdapter extends GsonPathTypeAdapter<TestBoxedPrimitives> {
    public TestBoxedPrimitives_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestBoxedPrimitives readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestBoxedPrimitives result = new TestBoxedPrimitives();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 9)) {
            switch (in.nextName()) {
                case "value1":
                    String value_value1 = GsonUtil.read(gson, String.class, gsonErrors, in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    Boolean value_value2 = GsonUtil.read(gson, Boolean.class, gsonErrors, in);
                    if (value_value2 != null) {
                        result.value2 = value_value2;
                    }
                    break;

                case "value3":
                    Integer value_value3 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_value3 != null) {
                        result.value3 = value_value3;
                    }
                    break;

                case "value4":
                    Double value_value4 = GsonUtil.read(gson, Double.class, gsonErrors, in);
                    if (value_value4 != null) {
                        result.value4 = value_value4;
                    }
                    break;

                case "value5":
                    Long value_value5 = GsonUtil.read(gson, Long.class, gsonErrors, in);
                    if (value_value5 != null) {
                        result.value5 = value_value5;
                    }
                    break;

                case "value6":
                    Byte value_value6 = GsonUtil.read(gson, Byte.class, gsonErrors, in);
                    if (value_value6 != null) {
                        result.value6 = value_value6;
                    }
                    break;

                case "value7":
                    Short value_value7 = GsonUtil.read(gson, Short.class, gsonErrors, in);
                    if (value_value7 != null) {
                        result.value7 = value_value7;
                    }
                    break;

                case "value8":
                    Float value_value8 = GsonUtil.read(gson, Float.class, gsonErrors, in);
                    if (value_value8 != null) {
                        result.value8 = value_value8;
                    }
                    break;

                case "value9":
                    Character value_value9 = GsonUtil.read(gson, Character.class, gsonErrors, in);
                    if (value_value9 != null) {
                        result.value9 = value_value9;
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
    public void writeImpl(JsonWriter out, TestBoxedPrimitives value) throws IOException {
        // Begin
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Boolean obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }

        Integer obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        Double obj3 = value.value4;
        if (obj3 != null) {
            out.name("value4");
            GsonUtil.writeWithGenericAdapter(gson, obj3.getClass(), out, obj3);
        }

        Long obj4 = value.value5;
        if (obj4 != null) {
            out.name("value5");
            GsonUtil.writeWithGenericAdapter(gson, obj4.getClass(), out, obj4);
        }

        Byte obj5 = value.value6;
        if (obj5 != null) {
            out.name("value6");
            GsonUtil.writeWithGenericAdapter(gson, obj5.getClass(), out, obj5);
        }

        Short obj6 = value.value7;
        if (obj6 != null) {
            out.name("value7");
            GsonUtil.writeWithGenericAdapter(gson, obj6.getClass(), out, obj6);
        }

        Float obj7 = value.value8;
        if (obj7 != null) {
            out.name("value8");
            GsonUtil.writeWithGenericAdapter(gson, obj7.getClass(), out, obj7);
        }

        Character obj8 = value.value9;
        if (obj8 != null) {
            out.name("value9");
            GsonUtil.writeWithGenericAdapter(gson, obj8.getClass(), out, obj8);
        }

        // End
        out.endObject();
    }
}