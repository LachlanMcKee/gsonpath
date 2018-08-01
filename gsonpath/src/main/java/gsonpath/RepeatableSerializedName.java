package gsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to reduce SerializedName repetition. This is especially helpful when duplicating jsonpath numerous times.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RepeatableSerializedName {
    /**
     * @return the desired name of the field when it is serialized or deserialized
     */
    String value();
}
