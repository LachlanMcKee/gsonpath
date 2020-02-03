package generator.standard.use_getter_annotation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class UseGetterAnnotationTest_Implementation_GsonTypeAdapter extends GsonPathTypeAdapter<UseGetterAnnotationTest.Implementation> {

    public UseGetterAnnotationTest_Implementation_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public UseGetterAnnotationTest.Implementation readImpl(JsonReader in) throws IOException {
        String value_common_name = null;
        int value_specific_intTest = 0;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);
        boolean[] mandatoryFieldsCheckList = new boolean[1];

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "common":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "name":
                                String value_common_name_safe = gson.getAdapter(String.class).read(in);
                                if (value_common_name_safe != null) {
                                    value_common_name = value_common_name_safe;
                                    mandatoryFieldsCheckList[0] = true;

                                } else {
                                    throw new gsonpath.JsonFieldNullException("common.name", "generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation");
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "specific":
                    while (jsonReaderHelper.handleObject(2, 1)) {
                        switch (in.nextName()) {
                            case "intTest":
                                value_specific_intTest = gson.getAdapter(Integer.class).read(in);
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(2);
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
                        fieldName = "common.name";
                        break;

                }
                throw new gsonpath.JsonFieldNoKeyException(fieldName, "generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation");
            }
        }
        return new UseGetterAnnotationTest.Implementation(
                value_common_name,
                value_specific_intTest);
    }

    @Override
    public void writeImpl(JsonWriter out, UseGetterAnnotationTest.Implementation value) throws
            IOException {
        // Begin
        out.beginObject();

        // Begin common
        out.name("common");
        out.beginObject();
        String obj0 = value.getName();
        if (obj0 != null) {
            out.name("name");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End common
        out.endObject();

        // Begin specific
        out.name("specific");
        out.beginObject();
        int obj1 = value.getIntTest();
        out.name("intTest");
        gson.getAdapter(Integer.class).write(out, obj1);

        // End specific
        out.endObject();
        // End
        out.endObject();
    }
}