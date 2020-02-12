package generator.standard.field_policy.validate_explicit_non_null;

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
public final class TestValidateWithDefaultValue_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateWithDefaultValue> {
    public TestValidateWithDefaultValue_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestValidateWithDefaultValue readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestValidateWithDefaultValue result = new TestValidateWithDefaultValue();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "mandatoryWithDefault":
                    Integer value_mandatoryWithDefault = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_mandatoryWithDefault != null) {
                        result.mandatoryWithDefault = value_mandatoryWithDefault;
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
    public void writeImpl(JsonWriter out, TestValidateWithDefaultValue value) throws IOException {
        // Begin
        out.beginObject();
        Integer obj0 = value.mandatoryWithDefault;
        if (obj0 != null) {
            out.name("mandatoryWithDefault");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End 
        out.endObject();
    }
}