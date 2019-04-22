package generator.interf.inheritance;

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
public final class TestUsingInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestUsingInheritance> {
    private static final int MANDATORY_INDEX_VALUE1 = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    public TestUsingInheritance_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestUsingInheritance readImpl(JsonReader in) throws IOException {
        Integer value_value3 = null;
        Integer value_value1 = null;
        Integer value_Json1_Nest2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 2, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "value3":
                    value_value3 = gson.getAdapter(Integer.class).read(in);
                    break;

                case "value1":
                    Integer value_value1_safe = gson.getAdapter(Integer.class).read(in);
                    if (value_value1_safe != null) {
                        value_value1 = value_value1_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_VALUE1] = true;

                    } else {
                        throw new gsonpath.JsonFieldNullException("value1", "generator.interf.inheritance.TestUsingInheritance_GsonPathModel");
                    }
                    break;

                case "Json1":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Nest2":
                                value_Json1_Nest2 = gson.getAdapter(Integer.class).read(in);
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
                    case MANDATORY_INDEX_VALUE1:
                        fieldName = "value1";
                        break;

                }
                throw new gsonpath.JsonFieldNoKeyException(fieldName, "generator.interf.inheritance.TestUsingInheritance_GsonPathModel");
            }
        }
        return new TestUsingInheritance_GsonPathModel(
                value_value3,
                value_value1,
                value_Json1_Nest2);
    }

    @Override
    public void writeImpl(JsonWriter out, TestUsingInheritance value) throws IOException {
        // Begin
        out.beginObject();
        Integer obj0 = value.getValue3();
        if (obj0 != null) {
            out.name("value3");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Integer obj1 = value.getValue1();
        if (obj1 != null) {
            out.name("value1");
            GsonUtil.writeWithGenericAdapter(gson, obj1.getClass(), out, obj1);
        }


        // Begin Json1
        out.name("Json1");
        out.beginObject();
        Integer obj2 = value.getValue2();
        if (obj2 != null) {
            out.name("Nest2");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End Json1
        out.endObject();
        // End
        out.endObject();
    }

    @Override
    public String getModelClassName() {
        return "generator.interf.inheritance.TestUsingInheritance_GsonPathModel";
    }
}