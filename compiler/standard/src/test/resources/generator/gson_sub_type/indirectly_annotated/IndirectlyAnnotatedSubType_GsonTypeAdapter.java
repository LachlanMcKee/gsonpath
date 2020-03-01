package generator.gson_sub_type.indirectly_annotated;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;

import java.io.IOException;
import java.lang.Class;
import java.lang.Override;

@GsonPathGenerated
public final class IndirectlyAnnotatedSubType_GsonTypeAdapter extends GsonPathTypeAdapter<IndirectlyAnnotatedSubType> {

    public IndirectlyAnnotatedSubType_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public IndirectlyAnnotatedSubType readImpl(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);

        JsonElement subTypeElement0_jsonElement = jsonElement.getAsJsonObject().get("type");
        final boolean subTypeElement0;
        if (subTypeElement0_jsonElement == null || subTypeElement0_jsonElement.isJsonNull()) {
            throw new com.google.gson.JsonParseException("cannot deserialize generator.gson_sub_type.indirectly_annotated.IndirectlyAnnotatedSubType because the subtype field 'type' is either null or does not exist.");
        } else {
            subTypeElement0 = gson.getAdapter(boolean.class).fromJsonTree(subTypeElement0_jsonElement);
        }

        Class<? extends IndirectlyAnnotatedSubType> delegateClass = IndirectlyAnnotatedSubType.getSubType1(subTypeElement0);
        if (delegateClass == null) {
            return null;
        }
        IndirectlyAnnotatedSubType result = gson.getAdapter(delegateClass).fromJsonTree(jsonElement);
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, IndirectlyAnnotatedSubType value) throws IOException {
        TypeAdapter delegateAdapter = gson.getAdapter(value.getClass());
        delegateAdapter.write(out, value);
    }
}