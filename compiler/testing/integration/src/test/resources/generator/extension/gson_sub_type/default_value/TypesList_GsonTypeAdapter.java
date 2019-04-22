package generator.extension.gson_sub_type.default_value;

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
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import gsonpath.internal.StrictArrayTypeAdapter;
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
public final class TypesList_GsonTypeAdapter extends GsonPathTypeAdapter<TypesList> {
    private StrictArrayTypeAdapter itemsGsonSubtype;

    public TypesList_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    private StrictArrayTypeAdapter getItemsGsonSubtype() {
        if (itemsGsonSubtype == null) {
            itemsGsonSubtype = new StrictArrayTypeAdapter<>(new ItemsGsonSubtype(gson), Type.class, false);
        }
        return itemsGsonSubtype;
    }

    @Override
    public TypesList readImpl(JsonReader in) throws IOException {
        TypesList result = new TypesList();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "items":
                    // Extension (Read) - 'GsonSubtype' Annotation
                    Type[] value_items = (Type[]) getItemsGsonSubtype().read(in);

                    if (value_items != null) {
                        result.items = value_items;
                    }
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TypesList value) throws IOException {
        // Begin
        out.beginObject();
        Type[] obj0 = value.items;
        if (obj0 != null) {
            out.name("items");
            // Extension (Write) - 'GsonSubtype' Annotation
            getItemsGsonSubtype().write(out, obj0);
        }

        // End
        out.endObject();
    }

    @Override
    public String getModelClassName() {
        return "generator.extension.gson_sub_type.default_value.TypesList";
    }

    private static final class ItemsGsonSubtype extends GsonPathTypeAdapter<Type> {
        private final Map<String, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

        private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

        private final TypeAdapter<? extends Type> defaultTypeAdapterDelegate;

        private ItemsGsonSubtype(Gson gson) {
            super(gson);
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put("type1", gson.getAdapter(Type1.class));
            typeAdaptersDelegatedByClassMap.put(Type1.class, gson.getAdapter(Type1.class));
            defaultTypeAdapterDelegate = gson.getAdapter(Type2.class);
        }

        @Override
        public Type readImpl(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().get("type");
            if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
                throw new JsonParseException("cannot deserialize generator.extension.gson_sub_type.Type because the subtype field 'type' is either null or does not exist.");
            }
            java.lang.String value = typeValueJsonElement.getAsString();
            TypeAdapter<? extends Type> delegate = typeAdaptersDelegatedByValueMap.get(value);
            if (delegate == null) {
                // Use the default type adapter if the type is unknown.
                delegate = defaultTypeAdapterDelegate;
            }
            Type result = delegate.fromJsonTree(jsonElement);
            return result;
        }

        @Override
        public void writeImpl(JsonWriter out, Type value) throws IOException {
            TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
            if (delegate == null) {
                delegate = defaultTypeAdapterDelegate;
            }
            delegate.write(out, value);
        }

        @Override
        public String getModelClassName() {
            return "generator.extension.gson_sub_type.Type";
        }
    }
}