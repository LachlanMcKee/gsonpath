package generator.enums;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
public final class TestEnum_GsonTypeAdapter extends TypeAdapter<TestEnum> {
    private final Gson mGson;
    private final Map<String, TestEnum> nameToConstant = new HashMap<String, TestEnum>();
    private final Map<TestEnum, String> constantToName = new HashMap<TestEnum, String>();

    public TestEnum_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;

        nameToConstant.put("value-abc", TestEnum.VALUE_ABC);
        nameToConstant.put("value-def", TestEnum.VALUE_DEF);
        nameToConstant.put("custom", TestEnum.VALUE_GHI);

        constantToName.put(TestEnum.VALUE_ABC, "value-abc");
        constantToName.put(TestEnum.VALUE_DEF, "value-def");
        constantToName.put(TestEnum.VALUE_GHI, "custom");
    }

    @Override
    public TestEnum read(JsonReader in) throws IOException {
        if (!isValidValue(in)) {
            return null;
        }
        return nameToConstant.get(in.nextString());
    }

    @Override
    public void write(JsonWriter out, TestEnum value) throws IOException {
        out.value(value == null ? null : constantToName.get(value));
    }
}