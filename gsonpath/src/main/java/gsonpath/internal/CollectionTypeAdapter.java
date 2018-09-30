package gsonpath.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection type adapter that delegates and read and write functions to a provided type adapter.
 * The underlying collection type is a {@link List}.
 *
 * @param <E> the object type to serialize/deserialize.
 */
public final class CollectionTypeAdapter<E> extends TypeAdapter<Collection<E>> {
    private final TypeAdapter<E> componentTypeAdapter;
    private final boolean filterNulls;

    public CollectionTypeAdapter(TypeAdapter<E> componentTypeAdapter, boolean filterNulls) {
        this.componentTypeAdapter = componentTypeAdapter;
        this.filterNulls = filterNulls;
    }

    @Override
    public Collection<E> read(JsonReader in) throws IOException {
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
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Collection<E> list) throws IOException {
        if (list == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (E element : list) {
            componentTypeAdapter.write(out, element);
        }
        out.endArray();
    }
}