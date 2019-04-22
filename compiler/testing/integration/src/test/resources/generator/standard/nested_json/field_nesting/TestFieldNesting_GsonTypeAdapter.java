package generator.standard.nested_json.field_nesting;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestFieldNesting_GsonTypeAdapter extends GsonPathTypeAdapter<TestFieldNesting> {
    public TestFieldNesting_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestFieldNesting readImpl(JsonReader in) throws IOException {
        TestFieldNesting result = new TestFieldNesting();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "Json1":
                    Integer value_Json1 = gson.getAdapter(Integer.class).read(in);
                    if (value_Json1 != null) {
                        result.value1 = value_Json1;
                    }
                    break;

                case "Json2":
                    while (jsonReaderHelper.handleObject(1, 2)) {
                        switch (in.nextName()) {
                            case "Nest1":
                                Integer value_Json2_Nest1 = gson.getAdapter(Integer.class).read(in);
                                if (value_Json2_Nest1 != null) {
                                    result.value2 = value_Json2_Nest1;
                                }
                                break;

                            case "Nest2":
                                while (jsonReaderHelper.handleObject(2, 2)) {
                                    switch (in.nextName()) {
                                        case "EndPoint1":
                                            Integer value_Json2_Nest2_EndPoint1 = gson.getAdapter(Integer.class).read(in);
                                            if (value_Json2_Nest2_EndPoint1 != null) {
                                                result.value3 = value_Json2_Nest2_EndPoint1;
                                            }
                                            break;

                                        case "EndPoint2":
                                            Integer value_Json2_Nest2_EndPoint2 = gson.getAdapter(Integer.class).read(in);
                                            if (value_Json2_Nest2_EndPoint2 != null) {
                                                result.value4 = value_Json2_Nest2_EndPoint2;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
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
    public void writeImpl(JsonWriter out, TestFieldNesting value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj0);


        // Begin Json2
        out.name("Json2");
        out.beginObject();
        int obj1 = value.value2;
        out.name("Nest1");
        gson.getAdapter(Integer.class).write(out, obj1);


        // Begin Json2Nest2
        out.name("Nest2");
        out.beginObject();
        int obj2 = value.value3;
        out.name("EndPoint1");
        gson.getAdapter(Integer.class).write(out, obj2);

        int obj3 = value.value4;
        out.name("EndPoint2");
        gson.getAdapter(Integer.class).write(out, obj3);

        // End Json2Nest2
        out.endObject();
        // End Json2
        out.endObject();
        // End
        out.endObject();
    }

    @Override
    public String getModelClassName() {
        return "generator.standard.nested_json.field_nesting.TestFieldNesting";
    }
}