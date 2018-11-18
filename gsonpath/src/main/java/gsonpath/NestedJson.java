package gsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to reduce SerializedName repetition. This is especially helpful when duplicating jsonpath numerous times.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface NestedJson {
    /**
     * @return the path of the nesting. Must be delimited by the correct character.
     */
    String value();
}
