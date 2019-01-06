package android.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({PARAMETER, LOCAL_VARIABLE, METHOD, FIELD, ANNOTATION_TYPE})
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