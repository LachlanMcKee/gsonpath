package generator.standard.custom_adapter_annotation;

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
public final class TestCustomAutoGsonAdapterModel_GsonTypeAdapter extends GsonPathTypeAdapter<TestCustomAutoGsonAdapterModel> {
    private static final int MANDATORY_INDEX_EXPECTEDVALUE = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    public TestCustomAutoGsonAdapterModel_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestCustomAutoGsonAdapterModel readImpl(JsonReader in) throws IOException {
        TestCustomAutoGsonAdapterModel result = new TestCustomAutoGsonAdapterModel();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "path":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "expectedValue":
                                Integer value_path_expectedValue = gson.getAdapter(Integer.class).read(in);
                                if (value_path_expectedValue != null) {
                                    result.expectedValue = value_path_expectedValue;
                                    mandatoryFieldsCheckList[MANDATORY_INDEX_EXPECTEDVALUE] = true;

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
        for (int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++) {

            // Check if a mandatory value is missing.
            if (!mandatoryFieldsCheckList[mandatoryFieldIndex]) {

                // Find the field name of the missing json value.
                String fieldName = null;
                switch (mandatoryFieldIndex) {
                    case MANDATORY_INDEX_EXPECTEDVALUE:
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

    @Override
    public String getModelClassName() {
        return "generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel";
    }
}