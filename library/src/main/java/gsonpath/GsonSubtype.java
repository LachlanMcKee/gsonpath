package gsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a developer to mark a gson field as heterogenous, and exposes the ability to map specific types to a value value
 * that is found within the field name defined within the model being deserialized.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface GsonSubtype {

    /**
     * The name of the field contained within the json object that is used to determine what the type should be
     * instantiated.
     *
     * @return a non-null field name that belongs to either a String, Integer or Boolean value.
     */
    String subTypeKey();

    /**
     * Determines the behaviour of the generated subtype adapter when an unknown subtype is found, or the subtype
     * fails to be deserailized (i.e. it returns a null value)
     *
     * @return the enum value which defines the outcome.
     */
    GsonSubTypeFailureOutcome subTypeFailureOutcome() default GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE;

    /**
     * The default type that is used if an unexpected value is encountered.
     * Note: This will only be used if {@link #subTypeFailureOutcome} is set to
     * {@link GsonSubTypeFailureOutcome#NULL_OR_DEFAULT_VALUE}
     *
     * @return the fall-back type to use.
     */
    Class defaultType() default void.class;

    /**
     * An array of string values and their related subtype class, this may be empty, however one of the 'values' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of string values and their related subtype class.
     */
    StringValueSubtype[] stringValueSubtypes() default {};

    /**
     * An array of integer values and their related subtype class, this may be empty, however one of the 'values' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of integer values and their related subtype class.
     */
    IntegerValueSubtype[] integerValueSubtypes() default {};

    /**
     * An array of boolean values and their related subtype class, this may be empty, however one of the 'values' arrays
     * must be assigned, otherwise the processor will fail.
     *
     * @return the array of boolean values and their related subtype class.
     */
    BooleanValueSubtype[] booleanValueSubtypes() default {};

    /**
     * Represents a string value and its related subtype class.
     * Whenever the value value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface StringValueSubtype {
        /**
         * The value that maps to the subtype.
         *
         * @return a non-null value.
         */
        String value();

        /**
         * The subtype that is used when the appropriate value is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }

    /**
     * Represents a integer value and its related subtype class.
     * Whenever the value value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface IntegerValueSubtype {
        /**
         * The value that maps to the subtype.
         *
         * @return a non-null value.
         */
        int value();

        /**
         * The subtype that is used when the appropriate value is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }

    /**
     * Represents a boolean value and its related subtype class.
     * Whenever the value value is found, the associated subtype will be instatiated.
     */
    @Retention(RetentionPolicy.SOURCE)
    @interface BooleanValueSubtype {
        /**
         * The value that maps to the subtype.
         *
         * @return a non-null value.
         */
        boolean value();

        /**
         * The subtype that is used when the appropriate value is found.
         * This class MUST be a subtype of either:
         * 1. The annotated field
         * 2. The method return type (for interfaces)
         * 3. The List type for interfaces that extend List
         *
         * @return a non-null class.
         */
        Class subtype();
    }
}
