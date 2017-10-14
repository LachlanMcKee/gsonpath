package generator.standard.polymorphism.default_value;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import generator.standard.polymorphism.Type;
import gsonpath.internal.StrictArrayTypeAdapter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public final class TypesList_GsonTypeAdapter extends TypeAdapter<TypesList> {
    private final Gson mGson;

    private StrictArrayTypeAdapter itemsGsonSubtype;

    public TypesList_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
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

                    generator.standard.polymorphism.Type[] value_items = (generator.standard.polymorphism.Type[]) getItemsGsonSubtype().read(in);
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
        Type[] obj0 = value.items;
        if (obj0 != null) {
            out.name("items");
            getItemsGsonSubtype().write(out, obj0);
        }

        // End
        out.endObject();
    }

    private StrictArrayTypeAdapter getItemsGsonSubtype() {
        if (itemsGsonSubtype == null) {
            itemsGsonSubtype = new StrictArrayTypeAdapter<>(new ItemsGsonSubtype(mGson), Type.class, false);
        }
        return itemsGsonSubtype;
    }

    private static final class ItemsGsonSubtype extends TypeAdapter<Type> {
        private final Map<String, TypeAdapter<? extends Type>> typeAdaptersDelegatedByValueMap;

        private final Map<Class<? extends Type>, TypeAdapter<? extends Type>> typeAdaptersDelegatedByClassMap;

        private final TypeAdapter<? extends Type> defaultTypeAdapterDelegate;

        private ItemsGsonSubtype(Gson gson) {
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put("type1", gson.getAdapter(generator.standard.polymorphism.Type1.class));
            typeAdaptersDelegatedByClassMap.put(generator.standard.polymorphism.Type1.class, gson.getAdapter(generator.standard.polymorphism.Type1.class));
            defaultTypeAdapterDelegate = gson.getAdapter(generator.standard.polymorphism.Type2.class);
        }

        @Override
        public Type read(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().remove("type");
            if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
                throw new JsonParseException("cannot deserialize generator.standard.polymorphism.Type because the subtype field 'type' is either null or does not exist.");
            }
            java.lang.String value = typeValueJsonElement.getAsString();
            TypeAdapter<? extends generator.standard.polymorphism.Type> delegate = typeAdaptersDelegatedByValueMap.get(value);
            if (delegate == null) {
                // Use the default type adapter if the type is unknown.
                delegate = defaultTypeAdapterDelegate;
            }
            generator.standard.polymorphism.Type result = delegate.fromJsonTree(jsonElement);
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
                // Use the default type adapter if the type is unknown.
                delegate = defaultTypeAdapterDelegate;
            }
            delegate.write(out, value);
        }
    }
}