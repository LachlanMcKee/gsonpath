package generator.standard.field_policy.validate_all_except_nullable;

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
public final class TestValidateAllExceptNullable_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateAllExceptNullable> {
    private static final int MANDATORY_INDEX_MANDATORY1 = 0;

    private static final int MANDATORY_INDEX_MANDATORY2 = 1;

    private static final int MANDATORY_FIELDS_SIZE = 2;

    public TestValidateAllExceptNullable_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestValidateAllExceptNullable readImpl(JsonReader in) throws IOException {
        TestValidateAllExceptNullable result = new TestValidateAllExceptNullable();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "mandatory1":
                    Integer value_mandatory1 = gson.getAdapter(Integer.class).read(in);
                    if (value_mandatory1 != null) {
                        result.mandatory1 = value_mandatory1;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_MANDATORY1] = true;

                    } else {
                        throw new gsonpath.JsonFieldNullException("mandatory1", "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable");
                    }
                    break;

                case "mandatory2":
                    Integer value_mandatory2 = gson.getAdapter(Integer.class).read(in);
                    if (value_mandatory2 != null) {
                        result.mandatory2 = value_mandatory2;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_MANDATORY2] = true;

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
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case MANDATORY_INDEX_MANDATORY1:
                        fieldName = "mandatory1";
                        break;

                    case MANDATORY_INDEX_MANDATORY2:
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

    @Override
    public String getModelClassName() {
        return "generator.standard.field_policy.validate_all_except_nullable.TestValidateAllExceptNullable";
    }
}