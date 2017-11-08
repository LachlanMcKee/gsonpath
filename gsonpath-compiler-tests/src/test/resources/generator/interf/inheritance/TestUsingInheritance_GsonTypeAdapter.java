package generator.interf.inheritance;

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
public final class TestUsingInheritance_GsonTypeAdapter extends TypeAdapter<TestUsingInheritance> {
    private static final int MANDATORY_INDEX_VALUE1 = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    private final Gson mGson;

    public TestUsingInheritance_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestUsingInheritance read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        java.lang.Integer value_value3 = null;
        java.lang.Integer value_value1 = null;
        java.lang.Integer value_Json1_Nest2 = null;
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "value3":
                    jsonFieldCounter0++;

                    value_value3 = getIntegerSafely(in);
                    break;

                case "value1":
                    jsonFieldCounter0++;

                    Integer value_value1_safe = getIntegerSafely(in);
                    if (value_value1_safe != null) {
                        value_value1 = value_value1_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_VALUE1] = true;

                    } else {
                        throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'value1' was null for class 'generator.interf.inheritance.TestUsingInheritance_GsonPathModel'");
                    }
                    break;

                case "Json1":
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
                            case "Nest2":
                                jsonFieldCounter1++;

                                value_Json1_Nest2 = getIntegerSafely(in);
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
                    case MANDATORY_INDEX_VALUE1:
                        fieldName = "value1";
                        break;

                }
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.interf.inheritance.TestUsingInheritance_GsonPathModel'");
            }
        }
        return new TestUsingInheritance_GsonPathModel(
                value_value3,
                value_value1,
                value_Json1_Nest2
        );
    }

    @Override
    public void write(JsonWriter out, TestUsingInheritance value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Integer obj0 = value.getValue3();
        if (obj0 != null) {
            out.name("value3");
            out.value(obj0);
        }

        Integer obj1 = value.getValue1();
        if (obj1 != null) {
            out.name("value1");
            out.value(obj1);
        }


        // Begin Json1
        out.name("Json1");
        out.beginObject();
        Integer obj2 = value.getValue2();
        if (obj2 != null) {
            out.name("Nest2");
            out.value(obj2);
        }

        // End Json1
        out.endObject();
        // End
        out.endObject();
    }
}