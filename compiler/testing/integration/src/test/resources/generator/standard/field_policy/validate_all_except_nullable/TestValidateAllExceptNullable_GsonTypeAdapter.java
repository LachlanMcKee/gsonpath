package generator.standard.field_policy.validate_all_except_nullable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GeneratedAdapter(adapterElementClassNames = {"generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable"})
public final class TestValidateAllExceptNullable_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateAllExceptNullable> {

    public TestValidateAllExceptNullable_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestValidateAllExceptNullable readImpl(JsonReader in) throws IOException {
        TestValidateAllExceptNullable result = new TestValidateAllExceptNullable();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[2];

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "mandatory1":
                    Integer value_mandatory1 = gson.getAdapter(Integer.class).read(in);
                    if (value_mandatory1 != null) {
                        result.mandatory1 = value_mandatory1;
                        mandatoryFieldsCheckList[0] = true;
                    } else {
                        throw new gsonpath.JsonFieldNullException("mandatory1", "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
                    }
                    break;

                case "mandatory2":
                    Integer value_mandatory2 = gson.getAdapter(Integer.class).read(in);
                    if (value_mandatory2 != null) {
                        result.mandatory2 = value_mandatory2;
                        mandatoryFieldsCheckList[1] = true;
                    } else {
                        throw new gsonpath.JsonFieldNullException("mandatory2", "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
                    }
                    break;

                case "optional1":
                    Integer value_optional1 = gson.getAdapter(Integer.class).read(in);
                    if (value_optional1 != null) {
                        result.optional1 = value_optional1;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 2; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case 0:
                        fieldName = "mandatory1";
                        break;

                    case 1:
                        fieldName = "mandatory2";
                        break;

                }
                throw new gsonpath.JsonFieldNoKeyException(fieldName, "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestValidateAllExceptNullable value) throws IOException {
        // Begin
        out.beginObject();
        Integer obj0 = value.mandatory1;
        if (obj0 != null) {
            out.name("mandatory1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Integer obj1 = value.mandatory2;
        if (obj1 != null) {
            out.name("mandatory2");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }

        Integer obj2 = value.optional1;
        if (obj2 != null) {
            out.name("optional1");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End
        out.endObject();
    }
}