package generator.standard.nested_json.field_nesting;

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
public final class TestFieldNesting_GsonTypeAdapter extends TypeAdapter<TestFieldNesting> {
    private final Gson mGson;

    public TestFieldNesting_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestFieldNesting read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestFieldNesting result = new TestFieldNesting();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "Json1":
                    jsonFieldCounter0++;

                    Integer value_Json1 = mGson.getAdapter(Integer.class).read(in);
                    if (value_Json1 != null) {
                        result.value1 = value_Json1;
                    }
                    break;

                case "Json2":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter1 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter1 == 2) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "Nest1":
                                jsonFieldCounter1++;

                                Integer value_Json2_Nest1 = mGson.getAdapter(Integer.class).read(in);
                                if (value_Json2_Nest1 != null) {
                                    result.value2 = value_Json2_Nest1;
                                }
                                break;

                            case "Nest2":
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
                                        case "EndPoint1":
                                            jsonFieldCounter2++;

                                            Integer value_Json2_Nest2_EndPoint1 = mGson.getAdapter(Integer.class).read(in);
                                            if (value_Json2_Nest2_EndPoint1 != null) {
                                                result.value3 = value_Json2_Nest2_EndPoint1;
                                            }
                                            break;

                                        case "EndPoint2":
                                            jsonFieldCounter2++;

                                            Integer value_Json2_Nest2_EndPoint2 = mGson.getAdapter(Integer.class).read(in);
                                            if (value_Json2_Nest2_EndPoint2 != null) {
                                                result.value4 = value_Json2_Nest2_EndPoint2;
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
    public void write(JsonWriter out, TestFieldNesting value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        mGson.getAdapter(Integer.class).write(out, obj0);


        // Begin Json2
        out.name("Json2");
        out.beginObject();
        int obj1 = value.value2;
        out.name("Nest1");
        mGson.getAdapter(Integer.class).write(out, obj1);


        // Begin Json2.Nest2
        out.name("Nest2");
        out.beginObject();
        int obj2 = value.value3;
        out.name("EndPoint1");
        mGson.getAdapter(Integer.class).write(out, obj2);

        int obj3 = value.value4;
        out.name("EndPoint2");
        mGson.getAdapter(Integer.class).write(out, obj3);

        // End Json2.Nest2
        out.endObject();
        // End Json2
        out.endObject();
        // End
        out.endObject();
    }
}