package gsonpath;

/**
 * Specifies what type of validation should be done during Gson parsing.
 * <p>
 * If some form of validation is used, and a field does not pass the validation, an exception
 * will be thrown and the parser will not return a result.
 */
public enum GsonFieldValidationType {
    /**
     * No validation will be done on fields, regardless of whether they use
     * 'nullable' or 'nonnull' annotations.
     */
    NO_VALIDATION,

    /**
     * All fields marked with non-null will be validated during parsing
     * if the field is not found, an exception will be thrown.
     * <p>
     * Primitive fields will also be treated as non-null. If you want to
     * ensure that a primitive can be optional, make it boxed.
     */
    VALIDATE_EXPLICIT_NON_NULL,

    /**
     * All fields will be validated unless they are marked with a 'nullable'
     * annotation.
     */
    VALIDATE_ALL_EXCEPT_NULLABLE
}
