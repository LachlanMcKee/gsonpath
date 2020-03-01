package generator.standard.custom_adapter_annotation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestCustomAutoGsonAdapterModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomAutoGsonAdapterModel> {
    public TestCustomAutoGsonAdapterModel_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestCustomAutoGsonAdapterModel readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestCustomAutoGsonAdapterModel result = new TestCustomAutoGsonAdapterModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "path":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "expectedValue":
                                Integer value_path_expectedValue = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                if (value_path_expectedValue != null) {
                                    result.expectedValue = value_path_expectedValue;
                                    mandatoryFieldsCheckList[0] = true;

                                } else {
                                    throw new gsonpath.exception.JsonFieldNullException("path$expectedValue", "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
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

        // Mandatory object validation
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < 1; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case 0:
                        fieldName = "path$expectedValue";
                        break;

                }
                throw new gsonpath.exception.JsonFieldNoKeyException(fieldName, "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestCustomAutoGsonAdapterModel value) throws IOException {
        // Begin
        out.beginObject();

        // Begin path
        out.name("path");
        out.beginObject();
        Integer obj0 = value.expectedValue;
        out.name("expectedValue");
        if (obj0 != null) {
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        } else {
            out.nullValue();
        }

        // End path
        out.endObject();
        // End
        out.endObject();
    }
}