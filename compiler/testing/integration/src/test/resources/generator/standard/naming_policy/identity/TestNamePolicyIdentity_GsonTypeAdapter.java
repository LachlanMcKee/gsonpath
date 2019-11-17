package generator.standard.naming_policy.identity;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GeneratedAdapter(
        adapterElementClassNames = {"generator.standard.naming_policy.identity.TestNamePolicyIdentity"}
)
public final class TestNamePolicyIdentity_GsonTypeAdapter extends GsonPathTypeAdapter<TestNamePolicyIdentity> {
    public TestNamePolicyIdentity_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestNamePolicyIdentity readImpl(JsonReader in) throws IOException {
        TestNamePolicyIdentity result = new TestNamePolicyIdentity();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "testValue":
                    Integer value_testValue = gson.getAdapter(Integer.class).read(in);
                    if (value_testValue != null) {
                        result.testValue = value_testValue;
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
    public void writeImpl(JsonWriter out, TestNamePolicyIdentity value) throws IOException {
        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("testValue");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}