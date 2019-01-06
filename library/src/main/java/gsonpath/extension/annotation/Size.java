package gsonpath.extension.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Size {
    /**
     * An exact size (or -1 if not specified)
     */
    long value() default -1;

    /**
     * A minimum size, inclusive
     */
    long min() default Long.MIN_VALUE;

    /**
     * A maximum size, inclusive
     */
    long max() default Long.MAX_VALUE;

    /**
     * The size must be a multiple of this factor
     */
    long multiple() default 1;
}