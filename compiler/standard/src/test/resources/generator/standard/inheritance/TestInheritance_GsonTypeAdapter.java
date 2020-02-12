package generator.standard.inheritance;

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
public final class TestInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestInheritance> {
    public TestInheritance_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestInheritance readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestInheritance result = new TestInheritance();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Json1":
                    Integer value_Json1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_Json1 != null) {
                        result.value1 = value_Json1;
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
    public void writeImpl(JsonWriter out, TestInheritance value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End 
        out.endObject();
    }
}