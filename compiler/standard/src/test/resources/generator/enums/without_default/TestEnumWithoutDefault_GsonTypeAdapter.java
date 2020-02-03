package generator.enums.without_default;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathErrorListener;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;

@GsonPathGenerated
public final class TestEnumWithoutDefault_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnumWithoutDefault> {
    public TestEnumWithoutDefault_GsonTypeAdapter(Gson gson, GsonPathErrorListener errorListener) {
        super(gson, errorListener);
    }

    @Override
    public TestEnumWithoutDefault readImpl(JsonReader in) throws IOException {
        String enumValue = in.nextString();
        switch (enumValue) {
            case "value-abc":
                return TestEnumWithoutDefault.VALUE_ABC;
            case "value-def":
                return TestEnumWithoutDefault.VALUE_DEF;
            case "custom":
                return TestEnumWithoutDefault.VALUE_GHI;
            case "value-1":
                return TestEnumWithoutDefault.VALUE_1;
            default:
                throw new gsonpath.JsonUnexpectedEnumValueException(enumValue, "generator.enums.without_default.TestEnumWithoutDefault");
        }
    }

    @Override
    public void writeImpl(JsonWriter out, TestEnumWithoutDefault value) throws IOException {
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