package generator.interf.valid;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathErrorListener;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestValidInterface_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidInterface> {
    public TestValidInterface_GsonTypeAdapter(Gson gson, GsonPathErrorListener errorListener) {
        super(gson, errorListener);
    }

    @Override
    public TestValidInterface readImpl(JsonReader in) throws IOException {
        Integer value_Json1_Nest1 = null;
        Integer value_value2 = null;
        Integer value_Json1_Nest3 = null;
        Integer value_result = null;
        Integer value_that = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);

        while (jsonReaderHelper.handleObject(0, 4)) {
            switch (in.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 2)) {
                        switch (in.nextName()) {
                            case "Nest1":
                                value_Json1_Nest1 = gson.getAdapter(Integer.class).read(in);
                                break;

                            case "Nest3":
                                value_Json1_Nest3 = gson.getAdapter(Integer.class).read(in);
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "value2":
                    value_value2 = gson.getAdapter(Integer.class).read(in);
                    break;

                case "result":
                    value_result = gson.getAdapter(Integer.class).read(in);
                    break;

                case "that":
                    value_that = gson.getAdapter(Integer.class).read(in);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestValidInterface_GsonPathModel(
                value_Json1_Nest1,
                value_value2,
                value_Json1_Nest3,
                value_result,
                value_that);
    }

    @Override
    public void writeImpl(JsonWriter out, TestValidInterface value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Json1
        out.name("Json1");
        out.beginObject();
        Integer obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("Nest1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Integer obj1 = value.getValue3();
        if (obj1 != null) {
            out.name("Nest3");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }

        // End Json1
        out.endObject();
        Integer obj2 = value.getValue2();
        if (obj2 != null) {
            out.name("value2");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        Integer obj3 = value.getResult();
        if (obj3 != null) {
            out.name("result");
            GsonUtil.writeWithGenericAdapter(gson, obj3.getClass(), out, obj3);
        }

        Integer obj4 = value.getThat();
        if (obj4 != null) {
            out.name("that");
            GsonUtil.writeWithGenericAdapter(gson, obj4.getClass(), out, obj4);
        }

        // End
        out.endObject();
    }
}