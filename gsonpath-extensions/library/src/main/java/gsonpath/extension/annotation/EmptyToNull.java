package gsonpath.extension.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Changes an empty object (of types map, list, array or string) to a null value.
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface EmptyToNull {
}