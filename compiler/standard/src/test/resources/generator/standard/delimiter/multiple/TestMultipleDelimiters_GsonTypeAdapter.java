package generator.standard.delimiter.multiple;

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
public final class TestMultipleDelimiters_GsonTypeAdapter extends GsonPathTypeAdapter<TestMultipleDelimiters> {
    public TestMultipleDelimiters_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestMultipleDelimiters readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestMultipleDelimiters result = new TestMultipleDelimiters();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Nest1":
                                Integer value_Json1_Nest1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
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

                case "Json2.Nest1":
                    Integer value_Json2_Nest1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_Json2_Nest1 != null) {
                        result.value2 = value_Json2_Nest1;
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
    public void writeImpl(JsonWriter out, TestMultipleDelimiters value) throws IOException {
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
        int obj1 = value.value2;
        out.name("Json2.Nest1");
        gson.getAdapter(Integer.class).write(out, obj1);

        // End 
        out.endObject();
    }
}