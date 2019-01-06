package gsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An interface annotated with this annotation will automatically generate a Gson {@link com.google.gson.TypeAdapterFactory}
 * at compile time.
 * <p>
 * This annotation exists to ensure developers are able to use GsonPath within multiple libraries/modules without any
 * interface name collisions (assuming the developer uses unique factory interface names, or package structures)
 * <p>
 * An example of how to use the annotation is as follows:
 * <pre>
 *
 * {@literal @}AutoGsonAdapterFactory
 *  public interface ProjectTypeAdapterFactory extends TypeAdapterFactory {
 *  {
 *  }
 * </pre>
 * <p>
 * This will then generated a concrete implementation of the {@link com.google.gson.TypeAdapterFactory} that should be
 * used when constructing the {@link com.google.gson.Gson} object. An example of how to do this is as follows:
 * <pre>
 *
 *  Gson gson = new GsonBuilder()
 *                  registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(ProjectTypeAdapterFactory.class));
 *                  .build();
 * </pre>
 * The above usage will use reflection to find the concrete implementation. If you wish to reference the class directly
 * to avoid adding an entry to proguard, you may use the following alternative:
 * <pre>
 *
 *  Gson gson = new GsonBuilder()
 *                  registerTypeAdapterFactory(new ProjectTypeAdapterFactoryImpl());
 *                  .build();
 * </pre>
 * Be aware that the ProjectTypeAdapterFactoryImpl class will not exist until the annotation processor has executed.
 * <p>
 * Note: This is a mandatory annotation if any usages of the {@link AutoGsonAdapter} are found.
 * The annotation processor will throw a compilation error if there is no interfaces annotated with this, or if there
 * is more than one interface per project.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoGsonAdapterFactory {
}
