package generator.standard.field_policy.validate_explicit_non_null;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestValidateExplicitNonNull_GsonTypeAdapter extends GsonPathTypeAdapter<TestValidateExplicitNonNull> {
    public TestValidateExplicitNonNull_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestValidateExplicitNonNull readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestValidateExplicitNonNull result = new TestValidateExplicitNonNull();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[3];

        while (jsonReaderHelper.handleObject(0, 4)) {
            switch (in.nextName()) {
                case "mandatory1":
                    Integer value_mandatory1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_mandatory1 != null) {
                        result.mandatory1 = value_mandatory1;
                        mandatoryFieldsCheckList[0] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("mandatory1", "generator.standard.field_policy.validate_explicit_non_null.TestValidateExplicitNonNull");
                    }
                    break;

                case "mandatory2":
                    Integer value_mandatory2 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_mandatory2 != null) {
                        result.mandatory2 = value_mandatory2;
                        mandatoryFieldsCheckList[1] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("mandatory2", "generator.standard.field_policy.validate_explicit_non_null.TestValidateExplicitNonNull");
                    }
                    break;

                case "mandatory3":
                    Integer value_mandatory3 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                    if (value_mandatory3 != null) {
                        result.mandatory3 = value_mandatory3;
                        mandatoryFieldsCheckList[2] = true;

                    } else {
                        throw new gsonpath.exception.JsonFieldNullException("mandatory3", "generator.standard.field_policy.validate_explicit_non_null.TestValidateExplicitNonNull");
                    }
                    break;

                case "optional1":
                    Integer value_optional1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
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
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 3; mandatoryFieldIndex++) {

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

                    case 2:
                        fieldName = "mandatory3";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.standard.field_policy.validate_explicit_non_null.TestValidateExplicitNonNull");
            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestValidateExplicitNonNull value) throws IOException {
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

        int obj2 = value.mandatory3;
        out.name("mandatory3");
        gson.getAdapter(Integer.class).write(out, obj2);

        Integer obj3 = value.optional1;
        if (obj3 != null) {
            out.name("optional1");
            GsonUtil.writeWithGenericAdapter(gson, obj3.getClass(), out, obj3);
        }

        // End
        out.endObject();
    }
}