package generator.gson_sub_type.indirectly_annotated;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class IndirectlyAnnotatedSubType_GsonTypeAdapter extends GsonPathTypeAdapter<IndirectlyAnnotatedSubType> {
    private final Map<Boolean, TypeAdapter<? extends IndirectlyAnnotatedSubType>> typeAdaptersDelegatedByValueMap;

    private final Map<Class<? extends IndirectlyAnnotatedSubType>, TypeAdapter<? extends IndirectlyAnnotatedSubType>> typeAdaptersDelegatedByClassMap;

    public IndirectlyAnnotatedSubType_GsonTypeAdapter(Gson gson) {
        super(gson);
        typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
        typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

        typeAdaptersDelegatedByValueMap.put(true, gson.getAdapter(IndirectlyAnnotatedSubType.Type1.class));
        typeAdaptersDelegatedByClassMap.put(IndirectlyAnnotatedSubType.Type1.class, gson.getAdapter(IndirectlyAnnotatedSubType.Type1.class));

        typeAdaptersDelegatedByValueMap.put(false, gson.getAdapter(IndirectlyAnnotatedSubType.Type2.class));
        typeAdaptersDelegatedByClassMap.put(IndirectlyAnnotatedSubType.Type2.class, gson.getAdapter(IndirectlyAnnotatedSubType.Type2.class));
    }

    @Override
    public IndirectlyAnnotatedSubType readImpl(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);
        JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().get("type");
        if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
            throw new JsonParseException("cannot deserialize generator.gson_sub_type.indirectly_annotated.IndirectlyAnnotatedSubType because the subtype field 'type' is either null or does not exist.");
        }
        boolean value = typeValueJsonElement.getAsBoolean();
        TypeAdapter<? extends IndirectlyAnnotatedSubType> delegate = typeAdaptersDelegatedByValueMap.get(value);
        if (delegate == null) {
            return null;
        }
        IndirectlyAnnotatedSubType result = delegate.fromJsonTree(jsonElement);
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, IndirectlyAnnotatedSubType value) throws IOException {
        TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
        delegate.write(out, value);
    }

    @Override
    public String getModelClassName() {
        return "generator.gson_sub_type.indirectly_annotated.IndirectlyAnnotatedSubType";
    }
}