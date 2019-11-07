package generator.gson_sub_type.one_argument;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TypeGsonSubType_GsonTypeAdapter extends GsonPathTypeAdapter<TypeGsonSubType> {

    public TypeGsonSubType_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TypeGsonSubType readImpl(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);

        JsonElement subTypeElement0_jsonElement = jsonElement.getAsJsonObject().get("type");
        final String subTypeElement0;
        if (subTypeElement0_jsonElement == null || subTypeElement0_jsonElement.isJsonNull()) {
            subTypeElement0 = null;
        } else {
            subTypeElement0 = gson.getAdapter(String.class).fromJsonTree(subTypeElement0_jsonElement);
        }

        Class<? extends TypeGsonSubType> delegateClass = TypeGsonSubType.getSubType1(subTypeElement0);
        if (delegateClass == null) {
            return null;
        }
        TypeGsonSubType result = gson.getAdapter(delegateClass).fromJsonTree(jsonElement);
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TypeGsonSubType value) throws IOException {
        TypeAdapter delegateAdapter = gson.getAdapter(value.getClass());
        delegateAdapter.write(out, value);
    }
}