package generator.interf.java8;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GeneratedAdapter(adapterElementClassNames = {"generator.interf.java8.TestJava8Interface_GsonPathModel", "generator.interf.java8.TestJava8Interface"})
public final class TestJava8Interface_GsonTypeAdapter extends GsonPathTypeAdapter<TestJava8Interface> {
    public TestJava8Interface_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestJava8Interface readImpl(JsonReader in) throws IOException {
        Integer value_value1 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    value_value1 = gson.getAdapter(Integer.class).read(in);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new TestJava8Interface_GsonPathModel(
                value_value1);
    }

    @Override
    public void writeImpl(JsonWriter out, TestJava8Interface value) throws IOException {
        // Begin
        out.beginObject();
        Integer obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End
        out.endObject();
    }
}