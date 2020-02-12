package generator.standard.naming_policy.upper_camel_case;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNamePolicyUpperCamelCase_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyUpperCamelCase> {
    public TestNamePolicyUpperCamelCase_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNamePolicyUpperCamelCase readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestNamePolicyUpperCamelCase result = new TestNamePolicyUpperCamelCase();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "TestValue":
                    Integer value_TestValue = GsonUtil.read(gson, Integer.class, gsonErrors, in);
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