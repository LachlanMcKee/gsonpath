package generator.standard.invalid.mutable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.extension.RemoveInvalidElementsUtil;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TestMutableRemoveInvalidElements_GsonTypeAdapter extends GsonPathTypeAdapter<TestMutableRemoveInvalidElements> {
    public TestMutableRemoveInvalidElements_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestMutableRemoveInvalidElements readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestMutableRemoveInvalidElements result = new TestMutableRemoveInvalidElements();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "value1":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    String[] value_value1 = RemoveInvalidElementsUtil.removeInvalidElementsArray(String.class, gson, in, gsonErrors, new RemoveInvalidElementsUtil.CreateArrayFunction<String>() {
                        @Override
                        public String[] createArray() {
                            return new String[0];
                        }
                    });

                    if (value_value1 != null) {
                        result.value1 = value_value1;
                    }
                    break;

                case "value2":
                    // Extension (Read) - 'RemoveInvalidElements' Annotation
                    List<String> value_value2 = RemoveInvalidElementsUtil.removeInvalidElementsList(String.class, gson, in, gsonErrors);

                    if (value_value2 != null) {
                        result.value2 = value_value2;
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
    public void writeImpl(JsonWriter out, TestMutableRemoveInvalidElements value) throws
            IOException {
        // Begin
        out.beginObject();
        String[] obj0 = value.value1;
        if (obj0 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        List<String> obj1 = value.value2;
        if (obj1 != null) {
            out.name("value2");
            gson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).write(out, obj1);
        }

        // End
        out.endObject();
    }
}