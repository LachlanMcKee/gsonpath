package generator.standard.polymorphism.with_other_elements;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import generator.standard.polymorphism.Type;
import generator.standard.polymorphism.Type1;
import generator.standard.polymorphism.Type2;
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
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "other1":
                    jsonFieldCounter0++;

                    String value_other1 = mGson.getAdapter(String.class).read(in);
                    if (value_other1 != null) {
                        result.other1 = value_other1;
                    }
                    break;

                case "items":
                    jsonFieldCounter0++;

                    Type[] value_items = (Type[]) getItemsGsonSubtype().read(in);
                    if (value_items != null) {
                        result.items = value_items;
                    }
                    break;

                case "other2":
                    jsonFieldCounter0++;

                    String value_other2 = mGson.getAdapter(String.class).read(in);
                    if (value_other2 != null) {
                        result.other2 = value_other2;
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
        String obj0 = value.other1;
        if (obj0 != null) {
            out.name("other1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        Type[] obj1 = value.items;
        if (obj1 != null) {
            out.name("items");
            getItemsGsonSubtype().write(out, obj1);
        }

        String obj2 = value.other2;
        if (obj2 != null) {
            out.name("other2");
            mGson.getAdapter(String.class).write(out, obj2);
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

        private ItemsGsonSubtype(Gson gson) {
            typeAdaptersDelegatedByValueMap = new java.util.HashMap<>();
            typeAdaptersDelegatedByClassMap = new java.util.HashMap<>();

            typeAdaptersDelegatedByValueMap.put("type1", gson.getAdapter(Type1.class));
            typeAdaptersDelegatedByClassMap.put(Type1.class, gson.getAdapter(Type1.class));

            typeAdaptersDelegatedByValueMap.put("type2", gson.getAdapter(Type2.class));
            typeAdaptersDelegatedByClassMap.put(Type2.class, gson.getAdapter(Type2.class));
        }

        @Override
        public Type read(JsonReader in) throws IOException {
            JsonElement jsonElement = Streams.parse(in);
            JsonElement typeValueJsonElement = jsonElement.getAsJsonObject().remove("type");
            if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull()) {
                throw new JsonParseException("cannot deserialize generator.standard.polymorphism.Type because the subtype field 'type' is either null or does not exist.");
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
        public void write(JsonWriter out, Type value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            TypeAdapter delegate = typeAdaptersDelegatedByClassMap.get(value.getClass());
            delegate.write(out, value);
        }
    }
}