package generator.standard.inheritance;

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
public final class TestInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestInheritance> {
    public TestInheritance_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestInheritance readImpl(JsonReader in) throws IOException {
        TestInheritance result = new TestInheritance();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Json1":
                    Integer value_Json1 = gson.getAdapter(Integer.class).read(in);
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

    @Override
    public String getModelClassName() {
        return "generator.standard.inheritance.TestInheritance";
    }
}