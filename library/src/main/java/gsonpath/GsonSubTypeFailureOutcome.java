package gsonpath;

/**
 * Defines the outcome of the GsonSubType generated code when the subtype is not found, either because an unexpected
 * subtype was found, or the delegated type adapter returned null.
 */
public enum GsonSubTypeFailureOutcome {
    /**
     * When an unexpected subtype is found, the value within the array will depend on whether the
     * {@link GsonSubtype#defaultType()} is specified. If it is not, a null value will always be assigned.
     * If the default type is supplied, this will be delegated to.
     * <p>
     * Note: A null value may still be returned if the default type cannot deserialize the object.
     */
    NULL_OR_DEFAULT_VALUE,

    /**
     * When an unexpected subtype is found, the element will be removed from the array / collection
     */
    REMOVE_ELEMENT,

    /**
     * When an unexpected subtype is found, an exception will be thrown, and the {@link com.google.gson.TypeAdapter}
     * will stop prematurely.
     */
    FAIL
}
