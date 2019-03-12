package generator.gson_sub_type;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Override;
import java.util.Map;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class class Type_GsonTypeAdapter extends TypeAdapter<Type> {
    private final Map<Boolean, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

    private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

    private ItemsGsonSubtype(Gson gson) {
        typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
        typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

        typeAdaptersDelegatedByValueMap.put(true, gson.getAdapter(Type1.class));
        typeAdaptersDelegatedByClassMap.put(Type1.class, gson.getAdapter(Type1.class));

        typeAdaptersDelegatedByValueMap.put(false, gson.getAdapter(Type2.class));
        typeAdaptersDelegatedByClassMap.put(Type2.class, gson.getAdapter(Type2.class));
    }

    @Override
    public Type read(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);
        JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().get("type");
        if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
            throw new JsonParseException("cannot deserialize generator.extension.gson_sub_type.Type because the subtype field 'type' is either null or does not exist.");
        }
        boolean value = typeValueJsonElement.getAsBoolean();
        TypeAdapter<? extends Type> delegate = typeAdaptersDelegatedByValueMap.get(value);
        if (delegate == null) {
            return null;
        }
        Type result = delegate.fromJsonTree(jsonElement);
        return result;
    }

    @Override
    public void write(JsonWriter out, Type value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
        delegate.write(out, value);
    }
}