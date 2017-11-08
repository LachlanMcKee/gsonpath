package generator.interf.primitive;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Override;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestUsingPrimitives_GsonTypeAdapter extends TypeAdapter<TestUsingPrimitives> {
    private static final int MANDATORY_INDEX_INTEXAMPLE = 0;
    private static final int MANDATORY_INDEX_LONGEXAMPLE = 1;
    private static final int MANDATORY_INDEX_DOUBLEEXAMPLE = 2;
    private static final int MANDATORY_INDEX_BOOLEANEXAMPLE = 3;

    private static final int MANDATORY_FIELDS_SIZE = 4;

    private final Gson mGson;

    public TestUsingPrimitives_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestUsingPrimitives read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }

        int value_intExample = 0;
        long value_longExample = 0L;
        double value_doubleExample = 0d;
        boolean value_booleanExample = false;
        int[] value_intArrayExample = null;
        long[] value_longArrayExample = null;
        double[] value_doubleArrayExample = null;
        boolean[] value_booleanArrayExample = null;

        boolean[] mandatoryFieldsCheckList = new boolean[MANDATORY_FIELDS_SIZE];

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 8) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "intExample":
                    jsonFieldCounter0++;

                    Integer value_intExample_safe = getIntegerSafely(in);
                    if (value_intExample_safe != null) {
                        value_intExample = value_intExample_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_INTEXAMPLE] = true;

                    } else {
                        throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'intExample' was null for class 'generator.interf.primitive.TestUsingPrimitives_GsonPathModel'");
                    }
                    break;

                case "longExample":
                    jsonFieldCounter0++;

                    Long value_longExample_safe = getLongSafely(in);
                    if (value_longExample_safe != null) {
                        value_longExample = value_longExample_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_LONGEXAMPLE] = true;

                    } else {
                        throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'longExample' was null for class 'generator.interf.primitive.TestUsingPrimitives_GsonPathModel'");
                    }
                    break;

                case "doubleExample":
                    jsonFieldCounter0++;

                    Double value_doubleExample_safe = getDoubleSafely(in);
                    if (value_doubleExample_safe != null) {
                        value_doubleExample = value_doubleExample_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_DOUBLEEXAMPLE] = true;

                    } else {
                        throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'doubleExample' was null for class 'generator.interf.primitive.TestUsingPrimitives_GsonPathModel'");
                    }
                    break;

                case "booleanExample":
                    jsonFieldCounter0++;

                    Boolean value_booleanExample_safe = getBooleanSafely(in);
                    if (value_booleanExample_safe != null) {
                        value_booleanExample = value_booleanExample_safe;
                        mandatoryFieldsCheckList[MANDATORY_INDEX_BOOLEANEXAMPLE] = true;

                    } else {
                        throw new gsonpath.JsonFieldMissingException("Mandatory JSON element 'booleanExample' was null for class 'generator.interf.primitive.TestUsingPrimitives_GsonPathModel'");
                    }
                    break;

                case "intArrayExample":
                    jsonFieldCounter0++;
                    value_intArrayExample = mGson.getAdapter(int[].class).read(in);
                    break;

                case "longArrayExample":
                    jsonFieldCounter0++;
                    value_longArrayExample = mGson.getAdapter(long[].class).read(in);
                    break;

                case "doubleArrayExample":
                    jsonFieldCounter0++;
                    value_doubleArrayExample = mGson.getAdapter(double[].class).read(in);
                    break;

                case "booleanArrayExample":
                    jsonFieldCounter0++;
                    value_booleanArrayExample = mGson.getAdapter(boolean[].class).read(in);
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
                    case MANDATORY_INDEX_INTEXAMPLE:
                        fieldName = "intExample";
                        break;

                    case MANDATORY_INDEX_LONGEXAMPLE:
                        fieldName = "longExample";
                        break;

                    case MANDATORY_INDEX_DOUBLEEXAMPLE:
                        fieldName = "doubleExample";
                        break;

                    case MANDATORY_INDEX_BOOLEANEXAMPLE:
                        fieldName = "booleanExample";
                        break;
                }
                throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class 'generator.interf.primitive.TestUsingPrimitives_GsonPathModel'");
            }
        }

        return new TestUsingPrimitives_GsonPathModel(
                value_intExample,
                value_longExample,
                value_doubleExample,
                value_booleanExample,
                value_intArrayExample,
                value_longArrayExample,
                value_doubleArrayExample,
                value_booleanArrayExample
        );
    }

    @Override
    public void write(JsonWriter out, TestUsingPrimitives value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.getIntExample();
        out.name("intExample");
        out.value(obj0);

        long obj1 = value.getLongExample();
        out.name("longExample");
        out.value(obj1);

        double obj2 = value.getDoubleExample();
        out.name("doubleExample");
        out.value(obj2);

        boolean obj3 = value.getBooleanExample();
        out.name("booleanExample");
        out.value(obj3);

        int[] obj4 = value.getIntArrayExample();
        if (obj4 != null) {
            out.name("intArrayExample");
            mGson.getAdapter(int[].class).write(out, obj4);
        }

        long[] obj5 = value.getLongArrayExample();
        if (obj5 != null) {
            out.name("longArrayExample");
            mGson.getAdapter(long[].class).write(out, obj5);
        }

        double[] obj6 = value.getDoubleArrayExample();
        if (obj6 != null) {
            out.name("doubleArrayExample");
            mGson.getAdapter(double[].class).write(out, obj6);
        }

        boolean[] obj7 = value.getBooleanArrayExample();
        if (obj7 != null) {
            out.name("booleanArrayExample");
            mGson.getAdapter(boolean[].class).write(out, obj7);
        }

        // End
        out.endObject();
    }
}