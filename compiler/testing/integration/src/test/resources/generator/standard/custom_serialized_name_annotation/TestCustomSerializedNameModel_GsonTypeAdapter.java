package generator.standard.custom_serialized_name_annotation;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "gsonpath.GsonProcessor",
    comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestCustomSerializedNameModel_GsonTypeAdapter extends TypeAdapter<TestCustomSerializedNameModel> {
    private final Gson mGson;

    public TestCustomSerializedNameModel_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestCustomSerializedNameModel read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestCustomSerializedNameModel result = new TestCustomSerializedNameModel();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "nest":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter1 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter1 == 5) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "value1":
                                jsonFieldCounter1++;

                                String value_nest_value1 = mGson.getAdapter(String.class).read(in);
                                if (value_nest_value1 != null) {
                                    result.value1 = value_nest_value1;
                                }
                                break;

                            case "value2":
                                jsonFieldCounter1++;

                                String value_nest_value2 = mGson.getAdapter(String.class).read(in);
                                if (value_nest_value2 != null) {
                                    result.valueX = value_nest_value2;
                                }
                                break;

                            case "second":
                                jsonFieldCounter1++;

                                // Ensure the object is not null.
                                if (!isValidValue(in)) {
                                    break;
                                }
                                int jsonFieldCounter2 = 0;
                                in.beginObject();

                                while (in.hasNext()) {
                                    if (jsonFieldCounter2 == 2) {
                                        in.skipValue();
                                        continue;
                                    }

                                    switch (in.nextName()) {
                                        case "value3":
                                            jsonFieldCounter2++;

                                            String value_nest_second_value3 = mGson.getAdapter(String.class).read(in);
                                            if (value_nest_second_value3 != null) {
                                                result.value3 = value_nest_second_value3;
                                            }
                                            break;

                                        case "value3b":
                                            jsonFieldCounter2++;

                                            String value_nest_second_value3b = mGson.getAdapter(String.class).read(in);
                                            if (value_nest_second_value3b != null) {
                                                result.value3b = value_nest_second_value3b;
                                            }
                                            break;

                                        default:
                                            in.skipValue();
                                            break;
                                    }
                                }

                                in.endObject();
                                break;

                            case "value1b":
                                jsonFieldCounter1++;

                                String value_nest_value1b = mGson.getAdapter(String.class).read(in);
                                if (value_nest_value1b != null) {
                                    result.value1b = value_nest_value1b;
                                }
                                break;

                            case "value2b":
                                jsonFieldCounter1++;

                                String value_nest_value2b = mGson.getAdapter(String.class).read(in);
                                if (value_nest_value2b != null) {
                                    result.valueXb = value_nest_value2b;
                                }
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject();
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
    public void write(JsonWriter out, TestCustomSerializedNameModel value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin nest
        out.name("nest");
        out.beginObject();
        String obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0);
        }

        String obj1 = value.valueX;
        if (obj1 != null) {
            out.name("value2");
            writeWithGenericAdapter(mGson, obj1.getClass(), out, obj1)
        }


        // Begin nestsecond
        out.name("second");
        out.beginObject();
        String obj2 = value.value3;
        if (obj2 != null) {
            out.name("value3");
            writeWithGenericAdapter(mGson, obj2.getClass(), out, obj2);
        }

        String obj3 = value.value3b;
        if (obj3 != null) {
            out.name("value3b");
            writeWithGenericAdapter(mGson, obj3.getClass(), out, obj3)
        }

        // End nestsecond
        out.endObject();
        String obj4 = value.value1b;
        if (obj4 != null) {
            out.name("value1b");
            writeWithGenericAdapter(mGson, obj4.getClass(), out, obj4)
        }

        String obj5 = value.valueXb;
        if (obj5 != null) {
            out.name("value2b");
            writeWithGenericAdapter(mGson, obj5.getClass(), out, obj5)
        }

        // End nest
        out.endObject();
        // End
        out.endObject();
    }
}