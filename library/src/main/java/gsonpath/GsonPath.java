package gsonpath;

import com.google.gson.TypeAdapterFactory;
import gsonpath.annotation.AutoGsonAdapter;
import gsonpath.annotation.AutoGsonAdapterFactory;

/**
 * The primary class to use when using the GsonPath library.
 * <p>
 * It supplies factories which expose auto generated class created using the {@link AutoGsonAdapter} annotation.
 */
public class GsonPath {
    private static final String FACTORY_IMPLEMENTATION_SUFFIX = "Impl";

    private GsonPath() {
    }

    /**
     * Creates an instance of an {@link TypeAdapterFactory} implementation class that implements the input interface.
     * <p>
     * This factory is used to map the auto generated {@link com.google.gson.TypeAdapter} classes created using the
     * {@link AutoGsonAdapter} annotation.
     * <p>
     * Only a single use of reflection is used within the constructor, so it isn't critical to hold onto this reference
     * for later usage.
     *
     * @param clazz the type adatper class to use to find the concrete implementation. Ensure that the interface is
     *              annotated with {@link AutoGsonAdapterFactory}
     * @return a new instance of the {@link TypeAdapterFactory} class
     */
    public static TypeAdapterFactory createTypeAdapterFactory(Class<? extends TypeAdapterFactory> clazz) {
        String factoryClassName = clazz.getCanonicalName() + FACTORY_IMPLEMENTATION_SUFFIX;
        try {
            return (TypeAdapterFactory) Class.forName(factoryClassName).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to instantiate generated TypeAdapterFactory '" + factoryClassName + "'", e);
        }
    }
}
