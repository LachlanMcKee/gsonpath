package gsonpath.extension.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Removes invalid elements from arrays/lists.
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface RemoveInvalidElements {
}