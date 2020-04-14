package gsonpath.audit;

import com.google.gson.stream.JsonReader;

import java.io.Reader;

/**
 * An implementation of the {@link JsonReader} that contains an audit log that records whenever a data-mapping
 * issue occurs.
 * <p>
 * Currently a data-mapping issue occurs when:
 * <ul>
 *     <li>A list element is removed from a list due to a parsing failure.</li>
 * </ul>
 */
public final class AuditJsonReader extends JsonReader {
    private AuditLog auditLog;

    public AuditJsonReader(Reader in) {
        super(in);
        this.auditLog = new AuditLog();
    }

    /**
     * @return the audit log for the reader.
     */
    public AuditLog getAuditLog() {
        return auditLog;
    }
}
