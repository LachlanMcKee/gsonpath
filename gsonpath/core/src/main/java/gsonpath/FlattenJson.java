package gsonpath;

import java.lang.annotation.*;

/**
 * Specifies that the JSON object found for this field should be
 * flattened into a String.
 * <p>
 * For example, given the following JSON:
 * <pre>
 * {
 *      'value1': 1,
 *      'value2': 2
 * }
 * </pre>
 * The following String is returned through the JsonParser:
 * <p>
 * "{'value1': 1, 'value2': 2}"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface FlattenJson {
}
