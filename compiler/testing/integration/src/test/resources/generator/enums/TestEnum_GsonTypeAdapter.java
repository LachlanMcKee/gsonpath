package generator.enums;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GeneratedAdapter;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;

@GeneratedAdapter(adapterElementClassNames = {"generator.enums.TestEnum"})
public final class TestEnum_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnum> {
    public TestEnum_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestEnum readImpl(JsonReader in) throws IOException {
        switch (in.nextString()) {
            case "value-abc":
                return generator.enums.TestEnum.VALUE_ABC;
            case "value-def":
                return generator.enums.TestEnum.VALUE_DEF;
            case "custom":
                return generator.enums.TestEnum.VALUE_GHI;
            case "value-1":
                return generator.enums.TestEnum.VALUE_1;
            default:
                return null;
        }
    }

    @Override
    public void writeImpl(JsonWriter out, TestEnum value) throws IOException {
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