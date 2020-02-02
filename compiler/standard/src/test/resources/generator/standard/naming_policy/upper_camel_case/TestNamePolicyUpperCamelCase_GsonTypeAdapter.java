package generator.standard.naming_policy.upper_camel_case;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNamePolicyUpperCamelCase_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyUpperCamelCase> {
    public TestNamePolicyUpperCamelCase_GsonTypeAdapter(GsonPathTypeAdapter.Arguments arguments) {
        super(arguments);
    }

    @Override
    public TestNamePolicyUpperCamelCase readImpl(JsonReader in) throws IOException {
        TestNamePolicyUpperCamelCase result = new TestNamePolicyUpperCamelCase();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "TestValue":
                    Integer value_TestValue = gson.getAdapter(Integer.class).read(in);
                    if (value_TestValue != null) {
                        result.testValue = value_TestValue;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestNamePolicyUpperCamelCase value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("TestValue");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}