package gsonpath;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static gsonpath.GsonUtil.isValidValue;

public abstract class GsonPathTypeAdapter<T> extends TypeAdapter<T> {
    protected final Gson gson;

    public GsonPathTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public final T read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        return readImpl(in);
    }

    @Override
    public final void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        writeImpl(out, value);
    }

    public abstract T readImpl(JsonReader in) throws IOException;

    public abstract void writeImpl(JsonWriter out, T value) throws IOException;
}
