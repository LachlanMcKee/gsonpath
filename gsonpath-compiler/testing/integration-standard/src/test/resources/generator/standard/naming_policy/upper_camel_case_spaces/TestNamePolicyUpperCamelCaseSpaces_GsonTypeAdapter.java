package generator.standard.naming_policy.upper_camel_case_spaces;

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
public final class TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter extends TypeAdapter<TestNamePolicyUpperCamelCaseSpaces> {
    private final Gson mGson;

    public TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestNamePolicyUpperCamelCaseSpaces read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestNamePolicyUpperCamelCaseSpaces result = new TestNamePolicyUpperCamelCaseSpaces();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "Test Value":
                    jsonFieldCounter0++;

                    Integer value_Test_Value = mGson.getAdapter(Integer.class).read(in);
                    if (value_Test_Value != null) {
                        result.testValue = value_Test_Value;
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
    public void write(JsonWriter out, TestNamePolicyUpperCamelCaseSpaces value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        int obj0 = value.testValue;
        out.name("Test Value");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End
        out.endObject();
    }
}