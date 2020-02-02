package generator.standard.custom_adapter_annotation;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestCustomAutoGsonAdapterModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomAutoGsonAdapterModel> {

    public TestCustomAutoGsonAdapterModel_GsonTypeAdapter(GsonPathTypeAdapter.Arguments arguments) {
        super(arguments);
    }

    @Override
    public TestCustomAutoGsonAdapterModel readImpl(JsonReader in) throws IOException {
        TestCustomAutoGsonAdapterModel result = new TestCustomAutoGsonAdapterModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "path":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "expectedValue":
                                Integer value_path_expectedValue = gson.getAdapter(Integer.class).read(in);
                                if (value_path_expectedValue != null) {
                                    result.expectedValue = value_path_expectedValue;
                                    mandatoryFieldsCheckList[0] = true;

                                } else {
                                    throw new gsonpath.JsonFieldNullException("path$expectedValue", "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
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
                throw new gsonpath.JsonFieldNoKeyException(fieldName, "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel");
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