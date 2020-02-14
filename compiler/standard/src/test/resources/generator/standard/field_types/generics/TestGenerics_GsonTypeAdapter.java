package generator.standard.field_types.generics;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TestGenerics_GsonTypeAdapter extends GsonPathTypeAdapter<TestGenerics> {
    public TestGenerics_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestGenerics readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestGenerics result = new TestGenerics();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "value1":
                    List<String> value_value1 = GsonUtil.read(gson, new com.google.gson.reflect.TypeToken<List<String>>(){}, gsonErrors, in);
                    if (value_value1 != null) {
                        result.value1 = value_value1;
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
    public void writeImpl(JsonWriter out, TestGenerics value) throws IOException {
        // Begin
        out.beginObject();
        List<String> obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            gson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj0);
        }

        // End
        out.endObject();
    }
}