package generator.gson_sub_type.two_arguments;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TypeGsonSubType_GsonTypeAdapter extends TypeAdapter<TypeGsonSubType> {
    private final Gson mGson;

    public TypeGsonSubType_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TypeGsonSubType read(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);

        JsonElement subTypeElement0_jsonElement = jsonElement.getAsJsonObject().get("type1");
        final String subTypeElement0;
        if (subTypeElement0_jsonElement == null || subTypeElement0_jsonElement.isJsonNull()) {
            throw new com.google.gson.JsonParseException("cannot deserialize generator.gson_sub_type.two_arguments.TypeGsonSubType because the subtype field 'type1' is either null or does not exist.");
        } else {
            subTypeElement0 = mGson.getAdapter(String.class).fromJsonTree(subTypeElement0_jsonElement);
        }

        JsonElement subTypeElement1_jsonElement = jsonElement.getAsJsonObject().get("type2");
        final List<String> subTypeElement1;
        if (subTypeElement1_jsonElement == null || subTypeElement1_jsonElement.isJsonNull()) {
            subTypeElement1 = null;
        } else {
            subTypeElement1 = mGson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).fromJsonTree(subTypeElement1_jsonElement);
        }

        Class<? extends TypeGsonSubType> delegateClass = TypeGsonSubType.getSubType1(subTypeElement0, subTypeElement1);
        if (delegateClass == null) {
            return null;
        }
        TypeGsonSubType result = mGson.getAdapter(delegateClass).fromJsonTree(jsonElement);
        return result;
    }

    @Override
    public void write(JsonWriter out, TypeGsonSubType value) throws IOException {

        if (value == null) {
            out.nullValue();
            return;
        }
        TypeAdapter delegateAdapter = mGson.getAdapter(value.getClass());
        delegateAdapter.write(out, value);
    }
}