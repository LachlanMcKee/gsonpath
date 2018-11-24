package android.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE})
public @interface FloatRange {
    /**
     * Smallest value. Whether it is inclusive or not is determined
     * by {@link #fromInclusive}
     */
    double from() default Double.NEGATIVE_INFINITY;

    /**
     * Largest value. Whether it is inclusive or not is determined
     * by {@link #toInclusive}
     */
    double to() default Double.POSITIVE_INFINITY;

    /**
     * Whether the from value is included in the range
     */
    boolean fromInclusive() default true;

    /**
     * Whether the to value is included in the range
     */
    boolean toInclusive() default true;
}