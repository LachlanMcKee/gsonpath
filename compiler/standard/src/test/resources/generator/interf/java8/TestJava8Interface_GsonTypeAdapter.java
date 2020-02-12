package generator.interf.java8;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestJava8Interface_GsonTypeAdapter extends GsonPathTypeAdapter<TestJava8Interface> {
    public TestJava8Interface_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestJava8Interface readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        Integer value_value1 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    value_value1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
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