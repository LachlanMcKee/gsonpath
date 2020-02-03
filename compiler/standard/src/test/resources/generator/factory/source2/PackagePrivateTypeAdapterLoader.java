package generator.factory.source2;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import gsonpath.GsonPathErrorListener;
import java.lang.Class;
import java.lang.Override;

public final class PackagePrivateTypeAdapterLoader implements TypeAdapterFactory {
    private final GsonPathErrorListener errorListener;

    public PackagePrivateTypeAdapterLoader(GsonPathErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = type.getRawType();
        if (rawType.equals(TestLoaderSource.class)) {
            return new TestLoaderSource_GsonTypeAdapter(gson, errorListener);

        } else if (rawType.equals(TestLoaderSource2.class)) {
            return new TestLoaderSource2_GsonTypeAdapter(gson, errorListener);
        }

        return null;
    }
}