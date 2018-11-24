package android.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE})
public @interface IntRange {
    /**
     * Smallest value, inclusive
     */
    long from() default Long.MIN_VALUE;

    /**
     * Largest value, inclusive
     */
    long to() default Long.MAX_VALUE;
}