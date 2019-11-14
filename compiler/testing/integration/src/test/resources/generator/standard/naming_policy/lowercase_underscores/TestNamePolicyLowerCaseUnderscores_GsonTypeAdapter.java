package generator.standard.naming_policy.lowercase_underscores;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyLowerCaseUnderscores> {
    public TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNamePolicyLowerCaseUnderscores readImpl(JsonReader in) throws IOException {
        TestNamePolicyLowerCaseUnderscores result = new TestNamePolicyLowerCaseUnderscores();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "test_value":
                    Integer value_test_value = gson.getAdapter(Integer.class).read(in);
                    if (value_test_value != null) {
                        result.testValue = value_test_value;
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
    public void writeImpl(JsonWriter out, TestNamePolicyLowerCaseUnderscores value) throws
            IOException {
        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("test_value");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}