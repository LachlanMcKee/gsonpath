package generator.standard.field_policy.validate_explicit_non_null;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
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
public final class TestValidateWithDefaultValue_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateWithDefaultValue> {
    public TestValidateWithDefaultValue_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestValidateWithDefaultValue readImpl(JsonReader in) throws IOException {
        TestValidateWithDefaultValue result = new TestValidateWithDefaultValue();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "mandatoryWithDefault":
                    Integer value_mandatoryWithDefault = gson.getAdapter(Integer.class).read(in);
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

    @Override
    public String getModelClassName() {
        return "generator.standard.field_policy.validate_explicit_non_null.TestValidateWithDefaultValue";
    }
}