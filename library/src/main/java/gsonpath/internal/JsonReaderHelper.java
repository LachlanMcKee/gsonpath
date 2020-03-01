package gsonpath.internal;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public final class JsonReaderHelper {
    private final JsonReader reader;
    private final int maxObjects;
    private final int maxArrays;

    private ObjectState[] objectStates;
    private Integer[] arrayIndexMap;

    public JsonReaderHelper(JsonReader reader, int maxObjects, int maxArrays) {
        this.reader = reader;
        this.maxObjects = maxObjects;
        this.maxArrays = maxArrays;
    }

    public final boolean handleObject(int index, int numberOfElements) throws IOException {
        ObjectState objectState = null;
        boolean stateArrayExists = objectStates != null;
        if (stateArrayExists) {
            objectState = objectStates[index];
        }

        if (objectState == null) {
            // If the object is null, abort
            if (!GsonUtil.isValidValue(reader)) {
                return false;
            }

            // Lazily create only when a valid object is found.
            if (!stateArrayExists) {
                objectStates = new ObjectState[maxObjects];
            }

            reader.beginObject();
            objectState = new ObjectState(numberOfElements);
            objectState.currentCounter = 0;
            objectState.fieldFound = true;

            objectStates[index] = objectState;
        } else {
            if (objectState.fieldFound) {
                objectState.currentCounter++;
            }
            objectState.fieldFound = true;
        }

        if (objectState.currentCounter == objectState.currentNumberOfElements) {
            while (reader.hasNext()) {
                reader.skipValue();
            }
        }

        boolean hasNext = reader.hasNext();
        if (!hasNext) {
            reader.endObject();
            objectStates[index] = null;
        }
        return hasNext;
    }

    public final void onObjectFieldNotFound(int index) throws IOException {
        reader.skipValue();
        objectStates[index].fieldFound = false;
    }

    public final boolean handleArray(int index) throws IOException {
        Integer arrayIndex = null;
        boolean stateMapExists = arrayIndexMap != null;
        if (stateMapExists) {
            arrayIndex = arrayIndexMap[index];
        }

        if (arrayIndex == null) {
            // If the array is null, abort
            if (!GsonUtil.isValidValue(reader)) {
                return false;
            }

            reader.beginArray();

            if (!stateMapExists) {
                arrayIndexMap = new Integer[maxArrays];
            }

            arrayIndexMap[index] = 0;
        } else {
            arrayIndexMap[index]++;
        }

        boolean hasNext = reader.hasNext();
        if (!hasNext) {
            reader.endArray();
            arrayIndexMap[index] = null;
        }
        return hasNext;
    }

    public final int getArrayIndex(int index) {
        return arrayIndexMap[index];
    }

    public final void onArrayFieldNotFound(int index) throws IOException {
        reader.skipValue();
    }

    private static final class ObjectState {
        private boolean fieldFound;
        private int currentCounter;
        private int currentNumberOfElements;

        ObjectState(int currentNumberOfElements) {
            this.currentNumberOfElements = currentNumberOfElements;
        }
    }
}