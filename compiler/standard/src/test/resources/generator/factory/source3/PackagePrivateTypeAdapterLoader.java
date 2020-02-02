package generator.factory.source3;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import gsonpath.GsonPathTypeAdapter;
import java.lang.Class;
import java.lang.Override;

public final class PackagePrivateTypeAdapterLoader implements TypeAdapterFactory {
    private final GsonPathTypeAdapter.Arguments arguments;

    public PackagePrivateTypeAdapterLoader(GsonPathTypeAdapter.Arguments arguments) {
        this.arguments = arguments;
    }

    @Override
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = type.getRawType();
        if (rawType.equals(TestLoaderSource.class)) {
            return new TestLoaderSource_GsonTypeAdapter(arguments);

        } else if (rawType.equals(TestLoaderSource.Inner.class)) {
            return new TestLoaderSource_Inner_GsonTypeAdapter(arguments);
        }

        return null;
    }
}