package gsonpath.extension;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import gsonpath.audit.AuditLog;
import gsonpath.audit.AuditLog.RemovedElement;
import gsonpath.audit.AuditJsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gsonpath.internal.GsonUtil.isValidValue;

public class RemoveInvalidElementsUtil {

    public static <T> void removeInvalidElementsList(TypeAdapter<T> adapter, JsonReader in, List<T> outputList) {
        AuditLog auditLog = AuditJsonReader.getAuditLogFromReader(in);

        JsonArray jsonArray = Streams.parse(in).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                outputList.add(adapter.fromJsonTree(jsonElement));
            } catch (JsonParseException e) {
                if (auditLog != null) {
                    auditLog.addRemovedElement(new RemovedElement(in.getPath(), e, jsonElement));
                }
            }
        }
    }

    public static <T> List<T> removeInvalidElementsList(Class<T> clazz, Gson gson, JsonReader in) throws IOException {
        if (!isValidValue(in)) {
            return null;
        }
        TypeAdapter<T> adapter = gson.getAdapter(clazz);
        List<T> elements = new ArrayList<>();
        removeInvalidElementsList(adapter, in, elements);
        return elements;
    }

    public static <T> T[] removeInvalidElementsArray(
            Class<T> clazz,
            Gson gson,
            JsonReader in,
            CreateArrayFunction<T> createArrayFunction) throws IOException {

        List<T> adjustedList = removeInvalidElementsList(clazz, gson, in);
        if (adjustedList == null) {
            return null;
        }
        return adjustedList.toArray(createArrayFunction.createArray());
    }

    public interface CreateArrayFunction<T> {
        T[] createArray();
    }
}
