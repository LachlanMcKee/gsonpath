package generator.standard.custom_adapter_annotation;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestCustomAutoGsonAdapterModel_GsonTypeAdapter extends TypeAdapter<TestCustomAutoGsonAdapterModel> {
    private static final int MANDATORY_INDEX_EXPECTEDVALUE = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    private final Gson mGson;

    public TestCustomAutoGsonAdapterModel_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestCustomAutoGsonAdapterModel read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestCustomAutoGsonAdapterModel result = new TestCustomAutoGsonAdapterModel();
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "path":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter1 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter1 == 1) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "expectedValue":
                                jsonFieldCounter1++;

                                Integer value_path_expectedValue = mGson.getAdapter(Integer.class).read(in);
                                if (value_path_expectedValue != null) {
                                    result.expectedValue = value_path_expectedValue;
                                    mandatoryFieldsCheckList[MANDATORY_INDEX_EXPECTEDVALUE] = true;

                                } else {
                                    throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'path$expectedValue' was null for class 'generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel'");
                                }
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }


                    in.endObject();
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }


        in.endObject();

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
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.standard.custom_adapter_annotation.TestCustomAutoGsonAdapterModel'");
            }
        }
        return result;
    }

    @Override
    public void write(JsonWriter out, TestCustomAutoGsonAdapterModel value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin path
        out.name("path");
        out.beginObject();
        Integer obj0 = value.expectedValue;
        out.name("expectedValue");
        if (obj0 != null) {
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0)
        } else {
            out.nullValue();
        }

        // End path
        out.endObject();
        // End
        out.endObject();
    }
}