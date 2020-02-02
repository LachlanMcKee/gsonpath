package gsonpath.safe;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathErrorListener;
import gsonpath.extension.RemoveInvalidElementsUtil;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static gsonpath.GsonUtil.isValidValue;

/**
 * A factory for the list that stores only valid results when parsing via Gson.
 * <p>
 * Any elements being deserialied that throw an exception are removed from the list.
 */
public final class GsonSafeListTypeAdapterFactory implements TypeAdapterFactory {
    private final GsonPathErrorListener errorListener;

    public GsonSafeListTypeAdapterFactory(GsonPathErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (GsonSafeList.class != typeToken.getRawType()) {
            return null;
        }

        Type elementType = ((ParameterizedType) typeToken.getType()).getActualTypeArguments()[0];
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));

        @SuppressWarnings({"unchecked", "rawtypes"})
        TypeAdapter<T> result = new Adapter(elementTypeAdapter, errorListener);
        return result;
    }

    private static final class Adapter<E> extends TypeAdapter<GsonSafeList<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
        private final GsonPathErrorListener errorListener;

        Adapter(TypeAdapter<E> elementTypeAdapter, GsonPathErrorListener errorListener) {
            this.elementTypeAdapter = elementTypeAdapter;
            this.errorListener = errorListener;
        }

        @Override
        public GsonSafeList<E> read(JsonReader in) throws IOException {
            if (!isValidValue(in)) {
                return null;
            }

            GsonSafeList<E> collection = new GsonSafeList<>();
            RemoveInvalidElementsUtil.removeInvalidElementsList(elementTypeAdapter, in, collection, errorListener);
            return collection;
        }

        @Override
        public void write(JsonWriter out, GsonSafeList<E> collection) throws IOException {
            throw new JsonIOException("Writing is not supported by GsonSafeArrayList");
        }
    }
}