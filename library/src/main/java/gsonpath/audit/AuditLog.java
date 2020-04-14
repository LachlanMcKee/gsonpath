package gsonpath.audit;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of any data mapping mismatches that occur during deserialization.
 */
public final class AuditLog {
    private List<RemovedElement> removedElements;

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
     * Returns a list of any elements were removed due to a failure during deserialization.
     */
    public List<RemovedElement> getRemovedElements() {
        return removedElements;
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
}
