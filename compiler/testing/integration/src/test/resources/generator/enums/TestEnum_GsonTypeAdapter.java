package generator.enums;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestEnum_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnum> {
    private final Map<String, TestEnum> nameToConstant = new HashMap<String, TestEnum>();
    private final Map<TestEnum, String> constantToName = new HashMap<TestEnum, String>();

    public TestEnum_GsonTypeAdapter(Gson gson) {
        super(gson);

        nameToConstant.put("value-abc", TestEnum.VALUE_ABC);
        nameToConstant.put("value-def", TestEnum.VALUE_DEF);
        nameToConstant.put("custom", TestEnum.VALUE_GHI);
        nameToConstant.put("value-1", TestEnum.VALUE_1);

        constantToName.put(TestEnum.VALUE_ABC, "value-abc");
        constantToName.put(TestEnum.VALUE_DEF, "value-def");
        constantToName.put(TestEnum.VALUE_GHI, "custom");
        constantToName.put(TestEnum.VALUE_1, "value-1");
    }

    @Override
    public TestEnum readImpl(JsonReader in) throws IOException {
        return nameToConstant.get(in.nextString());
    }

    @Override
    public void writeImpl(JsonWriter out, TestEnum value) throws IOException {
        out.value(constantToName.get(value));
    }
}