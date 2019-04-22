package generator.standard.delimiter.custom;

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
public final class TestCustomDelimiter_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomDelimiter> {
    public TestCustomDelimiter_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestCustomDelimiter readImpl(JsonReader in) throws IOException {
        TestCustomDelimiter result = new TestCustomDelimiter();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Nest1":
                                Integer value_Json1_Nest1 = gson.getAdapter(Integer.class).read(in);
                                if (value_Json1_Nest1 != null) {
                                    result.value1 = value_Json1_Nest1;
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
    public void writeImpl(JsonWriter out, TestCustomDelimiter value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Json1
        out.name("Json1");
        out.beginObject();
        int obj0 = value.value1;
        out.name("Nest1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Json1
        out.endObject();
        // End
        out.endObject();
    }

    @Override
    public String getModelClassName() {
        return "generator.standard.delimiter.custom.TestCustomDelimiter";
    }
}