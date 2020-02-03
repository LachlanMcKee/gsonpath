package generator.standard.field_policy.no_validation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNoValidation_GsonTypeAdapter extends GsonPathTypeAdapter<TestNoValidation> {
    public TestNoValidation_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public TestNoValidation readImpl(JsonReader in) throws IOException {
        TestNoValidation result = new TestNoValidation();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "optional1":
                    Integer value_optional1 = gson.getAdapter(Integer.class).read(in);
                    if (value_optional1 != null) {
                        result.optional1 = value_optional1;
                    }
                    break;

                case "optional2":
                    Integer value_optional2 = gson.getAdapter(Integer.class).read(in);
                    if (value_optional2 != null) {
                        result.optional2 = value_optional2;
                    }
                    break;

                case "optional3":
                    Integer value_optional3 = gson.getAdapter(Integer.class).read(in);
                    if (value_optional3 != null) {
                        result.optional3 = value_optional3;
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
    public void writeImpl(JsonWriter out, TestNoValidation value) throws IOException {
        // Begin
        out.beginObject();
        Integer obj0 = value.optional1;
        if (obj0 != null) {
            out.name("optional1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Integer obj1 = value.optional2;
        if (obj1 != null) {
            out.name("optional2");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }

        int obj2 = value.optional3;
        out.name("optional3");
        gson.getAdapter(Integer.class).write(out, obj2);

        // End
        out.endObject();
    }
}