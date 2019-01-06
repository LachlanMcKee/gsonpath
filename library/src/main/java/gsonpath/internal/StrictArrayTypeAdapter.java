package gsonpath.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * An array type adapter that is strict about the type being used, it will only use the type adapter being wrapped
 * and will not attempt to find a more appropriate model, unlike the {@link com.google.gson.internal.bind.ArrayTypeAdapter}
 * that this class is based on.
 *
 * @param <E> the object type to deserialize.
 */
public final class StrictArrayTypeAdapter<E> extends TypeAdapter<Object> {
    private final Class<E> componentType;
    private final boolean filterNulls;
    private final TypeAdapter<E> componentTypeAdapter;

    public StrictArrayTypeAdapter(TypeAdapter<E> componentTypeAdapter, Class<E> componentType, boolean filterNulls) {
        this.componentTypeAdapter = componentTypeAdapter;
        this.componentType = componentType;
        this.filterNulls = filterNulls;
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        List<E> list = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
            E instance = componentTypeAdapter.read(in);

            if (filterNulls && instance == null) {
                continue;
            }

            list.add(instance);
        }
        in.endArray();
        Object array = Array.newInstance(componentType, list.size());
        for (int i = 0; i < list.size(); i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (int i = 0, length = Array.getLength(array); i < length; i++) {
            E value = (E) Array.get(array, i);
            componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
}