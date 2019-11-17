package generator.gson_sub_type.two_arguments;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.List;

@GsonPathGenerated
public final class TypeGsonSubType_GsonTypeAdapter extends GsonPathTypeAdapter<TypeGsonSubType> {

    public TypeGsonSubType_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TypeGsonSubType readImpl(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);

        JsonElement subTypeElement0_jsonElement = jsonElement.getAsJsonObject().get("type1");
        final String subTypeElement0;
        if (subTypeElement0_jsonElement == null || subTypeElement0_jsonElement.isJsonNull()) {
            throw new com.google.gson.JsonParseException("cannot deserialize generator.gson_sub_type.two_arguments.TypeGsonSubType because the subtype field 'type1' is either null or does not exist.");
        } else {
            subTypeElement0 = gson.getAdapter(String.class).fromJsonTree(subTypeElement0_jsonElement);
        }

        JsonElement subTypeElement1_jsonElement = jsonElement.getAsJsonObject().get("type2");
        final List<String> subTypeElement1;
        if (subTypeElement1_jsonElement == null || subTypeElement1_jsonElement.isJsonNull()) {
            subTypeElement1 = null;
        } else {
            subTypeElement1 = gson.getAdapter(new com.google.gson.reflect.TypeToken<List<String>>(){}).fromJsonTree(subTypeElement1_jsonElement);
        }

        Class<? extends TypeGsonSubType> delegateClass = TypeGsonSubType.getSubType1(subTypeElement0, subTypeElement1);
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