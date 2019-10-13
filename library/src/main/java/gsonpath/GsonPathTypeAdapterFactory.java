package gsonpath;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import gsonpath.safe.GsonSafeListTypeAdapterFactory;

/**
 * Adds serialization/deserialization handling for GsonPath specific types to Gson.
 */
public final class GsonPathTypeAdapterFactory implements TypeAdapterFactory {
    private final GsonSafeListTypeAdapterFactory safeListFactory =
            new GsonSafeListTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        return safeListFactory.create(gson, typeToken);
    }
}