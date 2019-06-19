package generator.extension.gson_sub_type.null_string_key;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import generator.extension.gson_sub_type.Type;
import generator.extension.gson_sub_type.Type1;
import generator.extension.gson_sub_type.Type2;
import java.io.IOException;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TypesList_GsonTypeAdapter extends TypeAdapter<TypesList> {
    private final Gson mGson;

    private ItemsGsonSubtype itemsGsonSubtype;

    public TypesList_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    private ItemsGsonSubtype getItemsGsonSubtype() {
        if (itemsGsonSubtype == null) {
            itemsGsonSubtype = new ItemsGsonSubtype(mGson);
        }
        return itemsGsonSubtype;
    }

    @Override
    public TypesList read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TypesList result = new TypesList();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "items":
                    jsonFieldCounter0++;

                    // Extension (Read) - 'GsonSubtype' Annotation
                    Type value_items = (Type) getItemsGsonSubtype().read(in);

                    if (value_items != null) {
                        result.items = value_items;
                    }
                    break;

                default:
                    in.skipValue();
                    break;

            }
        }

        in.endObject();
        return result;
    }

    @Override
    public void write(JsonWriter out, TypesList value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        Type obj0 = value.items;
        if (obj0 != null) {
            out.name("items");
            // Extension (Write) - 'GsonSubtype' Annotation
            getItemsGsonSubtype().write(out, obj0);
        }

        // End
        out.endObject();
    }

    private static final class ItemsGsonSubtype extends TypeAdapter<Type> {
        private final Map<String, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

        private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

        private final TypeAdapter<? extends Type> defaultTypeAdapterDelegate;

        private ItemsGsonSubtype(Gson gson) {
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put(null, gson.getAdapter(Type2.class));
            typeAdaptersDelegatedByClassMap.put(Type2.class, gson.getAdapter(Type2.class));
            defaultTypeAdapterDelegate = gson.getAdapter(Type1.class);
        }

        @Override
        public Type read(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().get("type");
            final java.lang.String value;
            if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
                value = null;
            } else {
                value = typeValueJsonElement.getAsString();
            }
            TypeAdapter<? extends Type> delegate = typeAdaptersDelegatedByValueMap.get(value);
            if (delegate == null) {
                // Use the default type adapter if the type is unknown.
                delegate = defaultTypeAdapterDelegate;
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
            if (delegate == null) {
                delegate = defaultTypeAdapterDelegate;
            }
            delegate.write(out, value);
        }
    }
}