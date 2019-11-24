package generator.standard;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.Override;

public final class TestGsonTypeFactoryImpl implements TestGsonTypeFactory {
    private final TypeAdapterFactory[] mPackagePrivateLoaders;

    public GsonTypeAdapterLoader() {
        mPackagePrivateLoaders = new TypeAdapterFactory[0];
    }

    @Override
    public TypeAdapter create(Gson gson, TypeToken type) {
        for (int i = 0; i < mPackagePrivateLoaders.length; i++) {
            TypeAdapter typeAdapter = mPackagePrivateLoaders[i].create(gson, type);

            if (typeAdapter != null) {
                return typeAdapter;
            }
        }
        return null;
    }
}
