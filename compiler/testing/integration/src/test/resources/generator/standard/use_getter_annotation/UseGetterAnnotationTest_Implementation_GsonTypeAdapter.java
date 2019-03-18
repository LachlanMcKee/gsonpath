package generator.standard.use_getter_annotation;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "gsonpath.GsonProcessor",
    comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class UseGetterAnnotationTest_Implementation_GsonTypeAdapter extends TypeAdapter<UseGetterAnnotationTest.Implementation> {
    private static final int MANDATORY_INDEX_NAME = 0;

    private static final int MANDATORY_FIELDS_SIZE = 1;

    private final Gson mGson;

    public UseGetterAnnotationTest_Implementation_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public UseGetterAnnotationTest.Implementation read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        String value_common_name = null;
        int value_specific_intTest = 0;
        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "common":
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
                            case "name":
                                jsonFieldCounter1++;

                                String value_common_name_safe = mGson.getAdapter(String.class).read(in);
                                if (value_common_name_safe != null) {
                                    value_common_name = value_common_name_safe;
                                    mandatoryFieldsCheckList[MANDATORY_INDEX_NAME] = true;

                                } else {
                                    throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'common.name' was null for class 'generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation'");
                                }
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject();
                    break;

                case "specific":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter2 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter2 == 1) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "intTest":
                                jsonFieldCounter2++;

                                value_specific_intTest = mGson.getAdapter(Integer.class).read(in);
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
                    case MANDATORY_INDEX_NAME:
                        fieldName = "common.name";
                        break;

                }
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.standard.use_getter_annotation.UseGetterAnnotationTest.Implementation'");
            }
        }
        return new UseGetterAnnotationTest.Implementation(
            value_common_name,
            value_specific_intTest
        );
    }

    @Override
    public void write(JsonWriter out, UseGetterAnnotationTest.Implementation value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin common
        out.name("common");
        out.beginObject();
        String obj0 = value.getName();
        if (obj0 != null) {
            out.name("name");
            writeWithGenericAdapter(mGson, obj0.getClass(), out, obj0);
        }

        // End common
        out.endObject();

        // Begin specific
        out.name("specific");
        out.beginObject();
        int obj1 = value.getIntTest();
        out.name("intTest");
        mGson.getAdapter(Integer.class).write(out, obj1);

        // End specific
        out.endObject();
        // End
        out.endObject();
    }
}