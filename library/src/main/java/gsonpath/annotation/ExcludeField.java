package gsonpath.annotation;

import java.lang.annotation.*;

/**
 * Specifies that the annotated field will not be added to the auto generated
 * {@link com.google.gson.TypeAdapter} when using the {@link AutoGsonAdapter}
 * annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ExcludeField {
}
