package generator.enums.with_default;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestEnumWithDefault_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnumWithDefault> {
    public TestEnumWithDefault_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestEnumWithDefault readImpl(JsonReader in) throws IOException {
        String enumValue = in.nextString();
        switch (enumValue) {
            case "value-abc":
                return TestEnumWithDefault.VALUE_ABC;
            case "value-def":
                return TestEnumWithDefault.VALUE_DEF;
            case "custom":
                return TestEnumWithDefault.VALUE_GHI;
            case "value-1":
                return TestEnumWithDefault.VALUE_1;
            default:
                return TestEnumWithDefault.VALUE_ABC;
        }
    }

    @Override
    public void writeImpl(JsonWriter out, TestEnumWithDefault value) throws IOException {
        switch (value) {
            case VALUE_ABC:
                out.value("value-abc");
                break;
            case VALUE_DEF:
                out.value("value-def");
                break;
            case VALUE_GHI:
                out.value("custom");
                break;
            case VALUE_1:
                out.value("value-1");
                break;
        }
    }
}