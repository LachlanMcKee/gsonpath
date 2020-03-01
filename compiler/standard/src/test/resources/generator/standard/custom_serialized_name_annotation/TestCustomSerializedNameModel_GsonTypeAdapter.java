package generator.standard.custom_serialized_name_annotation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestCustomSerializedNameModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomSerializedNameModel> {
    public TestCustomSerializedNameModel_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestCustomSerializedNameModel readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestCustomSerializedNameModel result = new TestCustomSerializedNameModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "nest":
                    while (jsonReaderHelper.handleObject(1, 5)) {
                        switch (in.nextName()) {
                            case "value1":
                                String value_nest_value1 = GsonUtil.read(gson, String.class, gsonErrors, in);
                                if (value_nest_value1 != null) {
                                    result.value1 = value_nest_value1;
                                }
                                break;

                            case "value2":
                                String value_nest_value2 = GsonUtil.read(gson, String.class, gsonErrors, in);
                                if (value_nest_value2 != null) {
                                    result.valueX = value_nest_value2;
                                }
                                break;

                            case "second":
                                while (jsonReaderHelper.handleObject(2, 2)) {
                                    switch (in.nextName()) {
                                        case "value3":
                                            String value_nest_second_value3 = GsonUtil.read(gson, String.class, gsonErrors, in);
                                            if (value_nest_second_value3 != null) {
                                                result.value3 = value_nest_second_value3;
                                            }
                                            break;

                                        case "value3b":
                                            String value_nest_second_value3b = GsonUtil.read(gson, String.class, gsonErrors, in);
                                            if (value_nest_second_value3b != null) {
                                                result.value3b = value_nest_second_value3b;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
                                }
                                break;

                            case "value1b":
                                String value_nest_value1b = GsonUtil.read(gson, String.class, gsonErrors, in);
                                if (value_nest_value1b != null) {
                                    result.value1b = value_nest_value1b;
                                }
                                break;

                            case "value2b":
                                String value_nest_value2b = GsonUtil.read(gson, String.class, gsonErrors, in);
                                if (value_nest_value2b != null) {
                                    result.valueXb = value_nest_value2b;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
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
    public void writeImpl(JsonWriter out, TestCustomSerializedNameModel value) throws IOException {
        // Begin
        out.beginObject();

        // Begin nest
        out.name("nest");
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        String obj1 = value.valueX;
        if (obj1 != null) {
            out.name("value2");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }


        // Begin nestsecond
        out.name("second");
        out.beginObject();
        String obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        String obj3 = value.value3b;
        if (obj3 != null) {
            out.name("value3b");
            GsonUtil.writeWithGenericAdapter(gson, obj3.getClass(), out, obj3);
        }

        // End nestsecond
        out.endObject();
        String obj4 = value.value1b;
        if (obj4 != null) {
            out.name("value1b");
            GsonUtil.writeWithGenericAdapter(gson, obj4.getClass(), out, obj4);
        }

        String obj5 = value.valueXb;
        if (obj5 != null) {
            out.name("value2b");
            GsonUtil.writeWithGenericAdapter(gson, obj5.getClass(), out, obj5);
        }

        // End nest
        out.endObject();
        // End
        out.endObject();
    }
}