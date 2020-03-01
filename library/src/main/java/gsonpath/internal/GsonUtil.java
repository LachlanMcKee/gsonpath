package gsonpath.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.GsonSafeList;
import gsonpath.internal.adapter.ArrayTypeAdapter;
import gsonpath.internal.adapter.CollectionTypeAdapter;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import gsonpath.internal.adapter.MapTypeAdapter;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * A set of Gson utilities which expose functionality to read content from a JsonReader
 * safely without throwing IOExceptions.
 */
public class GsonUtil {

    private GsonUtil() {
    }

    /**
     * Determines whether the next value within the reader is not null.
     *
     * @param in the json reader used to read the stream
     * @return true if a valid value exists, or false if the value is null.
     * @throws IOException see {@link JsonReader#skipValue()}
     */
    public static boolean isValidValue(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.skipValue();
            return false;
        }
        return true;
    }

    public static <T> T read(Gson gson, Class<T> type, GsonErrors gsonErrors, JsonReader in) throws IOException {
        return read(gson, TypeToken.get(type), gsonErrors, in);
    }

    public static <T> T read(
            Gson gson,
            TypeToken<T> typeToken,
            GsonErrors gsonErrors,
            JsonReader in
    ) throws IOException {
        T arrayResult = handleArrayRead(gson, typeToken, gsonErrors, in);
        if (arrayResult != null) {
            return arrayResult;
        }

        T collectionResult = handleCollectionRead(gson, typeToken, gsonErrors, in);
        if (collectionResult != null) {
            return collectionResult;
        }

        T mapResult = handleMapRead(gson, typeToken, gsonErrors, in);
        if (mapResult != null) {
            return mapResult;
        }

        TypeAdapter<T> adapter = gson.getAdapter(typeToken);
        if (gsonErrors != null) {
            if (adapter instanceof GsonPathTypeAdapter) {
                return ((GsonPathTypeAdapter<T>) adapter).readImpl(in, gsonErrors);
            } else {
                return adapter.read(in);
            }
        } else {
            return adapter.read(in);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleArrayRead(
            Gson gson,
            TypeToken<T> typeToken,
            GsonErrors gsonErrors,
            JsonReader in
    ) throws IOException {
        Type type = typeToken.getType();
        if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
            return null;
        }
        Type componentType = $Gson$Types.getArrayComponentType(type);
        TypeAdapter<?> delegatingAdapter = gson.getAdapter(TypeToken.get(componentType));
        if (!(delegatingAdapter instanceof GsonPathTypeAdapter)) {
            return null;
        }
        return (T) new ArrayTypeAdapter(gson, (GsonPathTypeAdapter<?>) delegatingAdapter, $Gson$Types.getRawType(componentType))
                .read(in, gsonErrors);
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleCollectionRead(
            Gson gson,
            TypeToken<T> typeToken,
            GsonErrors gsonErrors,
            JsonReader in
    ) throws IOException {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType) || rawType == GsonSafeList.class) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> delegatingAdapter = gson.getAdapter(TypeToken.get(elementType));
        if (!(delegatingAdapter instanceof GsonPathTypeAdapter)) {
            return null;
        }

        ObjectConstructor<T> constructor = new ConstructorConstructor(Collections.emptyMap()).get(typeToken);
        return (T) new CollectionTypeAdapter(gson, (GsonPathTypeAdapter<?>) delegatingAdapter, constructor)
                .read(in, gsonErrors);
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleMapRead(
            Gson gson,
            TypeToken<T> typeToken,
            GsonErrors gsonErrors,
            JsonReader in
    ) throws IOException {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        }

        Class<?> rawTypeOfSrc = $Gson$Types.getRawType(type);
        Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
        TypeAdapter<?> keyAdapter = getKeyAdapter(gson, keyAndValueTypes[0]);
        TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
        ObjectConstructor<T> constructor = new ConstructorConstructor(Collections.emptyMap()).get(typeToken);

        return (T) new MapTypeAdapter(gson, keyAdapter, valueAdapter, constructor)
                .read(in, gsonErrors);
    }

    /**
     * Returns a type adapter that writes the value as a string.
     */
    private static TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
        return (keyType == boolean.class || keyType == Boolean.class)
                ? TypeAdapters.BOOLEAN_AS_STRING
                : context.getAdapter(TypeToken.get(keyType));
    }

    public static <T> T fromJsonTree(Gson gson, Class<T> type, GsonErrors gsonErrors, JsonElement jsonTree) {
        return fromJsonTree(gson, TypeToken.get(type), gsonErrors, jsonTree);
    }

    public static <T> T fromJsonTree(Gson gson, TypeToken<T> type, GsonErrors gsonErrors, JsonElement jsonTree) {
        try {
            JsonReader jsonReader = new JsonTreeReader(jsonTree);
            return read(gson, type, gsonErrors, jsonReader);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeWithGenericAdapter(
            Gson gson,
            Class<? extends T> clazz,
            JsonWriter out,
            T obj0
    ) throws IOException {
        TypeAdapter adapter = gson.getAdapter(clazz);
        adapter.write(out, obj0);
    }
}
