package generator.standard.naming_policy.lowercase_underscores;

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
public final class TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter extends TypeAdapter<TestNamePolicyLowerCaseUnderscores> {
    private final Gson mGson;

    public TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestNamePolicyLowerCaseUnderscores read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestNamePolicyLowerCaseUnderscores result = new TestNamePolicyLowerCaseUnderscores();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "test_value":
                    jsonFieldCounter0++;

                    Integer value_test_value = mGson.getAdapter(Integer.class).read(in);
                    if (value_test_value != null) {
                        result.testValue = value_test_value;
                    }
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }


        in.endObject();
        return result;
    }

    @Override
    public void write(JsonWriter out, TestNamePolicyLowerCaseUnderscores value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("test_value");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}