package gsonpath.audit;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of any data mapping mismatches that occur during deserialization.
 */
public final class AuditLog {
    private List<RemovedElement> removedElements;
    private List<UnexpectedEnumValue> unexpectedEnumValues;

    AuditLog() {
    }

    /**
     * Track an element that was removed due to a failure during deserialization.
     */
    public void addRemovedElement(RemovedElement removedElement) {
        if (removedElements == null) {
            removedElements = new ArrayList<>();
        }
        removedElements.add(removedElement);
    }

    /**
     * Track when a default enum value was used due to an unexpected value during deserialization.
     */
    public void addUnexpectedEnumValue(UnexpectedEnumValue unexpectedEnumValue) {
        if (unexpectedEnumValues == null) {
            unexpectedEnumValues = new ArrayList<>();
        }
        unexpectedEnumValues.add(unexpectedEnumValue);
    }

    /**
     * Returns a list of elements that were removed due to a failure during deserialization.
     */
    public List<RemovedElement> getRemovedElements() {
        return removedElements;
    }

    /**
     * Returns a list of unexpected enum values.
     */
    public List<UnexpectedEnumValue> getUnexpectedEnumValues() {
        return unexpectedEnumValues;
    }

    /**
     * Metadata associated with an element that failed during deserialization.
     */
    public static final class RemovedElement {
        public final String path;
        public final Exception exception;
        public final JsonElement jsonElement;

        public RemovedElement(String path, Exception exception, JsonElement jsonElement) {
            this.path = path;
            this.exception = exception;
            this.jsonElement = jsonElement;
        }
    }

    /**
     * Metadata associated with an unexpected enum value during deserialization.
     */
    public static final class UnexpectedEnumValue {
        public final String typeName;
        public final String path;
        public final String value;

        public UnexpectedEnumValue(String typeName, String path, String value) {
            this.typeName = typeName;
            this.path = path;
            this.value = value;
        }
    }
}
