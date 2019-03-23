package generator.enums;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Generated;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static gsonpath.GsonUtil.isValidValue;

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

        nameToConstant.put("value-1", TestEnum.VALUE_1);
        nameToConstant.put("value-2", TestEnum.VALUE_2);
        nameToConstant.put("custom", TestEnum.VALUE_3);

        constantToName.put(TestEnum.VALUE_1, "value-1");
        constantToName.put(TestEnum.VALUE_2, "value-2");
        constantToName.put(TestEnum.VALUE_3, "custom");
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