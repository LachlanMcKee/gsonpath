package generator.standard.naming_policy.lowercase_dashes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestNamePolicyLowerCaseDashes_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyLowerCaseDashes> {
    public TestNamePolicyLowerCaseDashes_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNamePolicyLowerCaseDashes readImpl(JsonReader in, GsonErrors gsonErrors) throws
            IOException {
        TestNamePolicyLowerCaseDashes result = new TestNamePolicyLowerCaseDashes();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "test-value":
                    Integer value_test_value = GsonUtil.read(gson, Integer.class, gsonErrors, in);
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
    public void writeImpl(JsonWriter out, TestNamePolicyLowerCaseDashes value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("test-value");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}