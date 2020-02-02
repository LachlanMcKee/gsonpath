package generator.interf.valid;

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
        if (rawType.equals(TestValidInterface_GsonPathModel.class) || rawType.equals(TestValidInterface.class)) {
            return new TestValidInterface_GsonTypeAdapter(arguments);
        }

        return null;
    }
}