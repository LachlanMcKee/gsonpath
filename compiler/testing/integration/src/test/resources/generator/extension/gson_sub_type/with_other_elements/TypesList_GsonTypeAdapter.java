package generator.extension.gson_sub_type.with_other_elements;

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
import gsonpath.GsonUtil;
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

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "other1":
                    String value_other1 = gson.getAdapter(String.class).read(in);
                    if (value_other1 != null) {
                        result.other1 = value_other1;
                    }
                    break;

                case "items":
                    // Extension (Read) - 'GsonSubtype' Annotation
                    Type[] value_items = (Type[]) getItemsGsonSubtype().read(in);

                    if (value_items != null) {
                        result.items = value_items;
                    }
                    break;

                case "other2":
                    String value_other2 = gson.getAdapter(String.class).read(in);
                    if (value_other2 != null) {
                        result.other2 = value_other2;
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
        String obj0 = value.other1;
        if (obj0 != null) {
            out.name("other1");
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        Type[] obj1 = value.items;
        if (obj1 != null) {
            out.name("items");
            // Extension (Write) - 'GsonSubtype' Annotation
            getItemsGsonSubtype().write(out, obj1);
        }

        String obj2 = value.other2;
        if (obj2 != null) {
            out.name("other2");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End
        out.endObject();
    }

    private static final class ItemsGsonSubtype extends GsonPathTypeAdapter<Type> {
        private final Map<String, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

        private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

        private ItemsGsonSubtype(Gson gson) {
            super(gson);
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put("type1", gson.getAdapter(Type1.class));
            typeAdaptersDelegatedByClassMap.put(Type1.class, gson.getAdapter(Type1.class));

            typeAdaptersDelegatedByValueMap.put("type2", gson.getAdapter(Type2.class));
            typeAdaptersDelegatedByClassMap.put(Type2.class, gson.getAdapter(Type2.class));
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
                return null;
            }
            Type result = delegate.fromJsonTree(jsonElement);
            return result;
        }

        @Override
        public void writeImpl(JsonWriter out, Type value) throws IOException {
            TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
            delegate.write(out, value);
        }
    }
}