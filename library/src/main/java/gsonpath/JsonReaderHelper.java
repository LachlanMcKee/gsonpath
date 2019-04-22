package gsonpath;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public final class JsonReaderHelper {
    private static final int ARRAY_INDEX_NOT_SET = -1;

    private final JsonReader reader;
    private final int maxObjects;
    private final int maxArrays;

    private ObjectState[] objectStates;
    private int[] arrayIndexMap;

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
        } else {
            if (objectState.fieldFound) {
                objectState.currentCounter++;
            }
            objectState.fieldFound = true;
        }

        boolean hasNext = reader.hasNext();
        if (hasNext) {
            if (objectState.currentCounter == objectState.currentNumberOfElements) {
                reader.skipValue();
            }
        } else {
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
        int arrayIndex = ARRAY_INDEX_NOT_SET;
        boolean stateMapExists = arrayIndexMap != null;
        if (stateMapExists) {
            arrayIndex = arrayIndexMap[index];
        }

        if (arrayIndex == ARRAY_INDEX_NOT_SET) {
            reader.beginArray();

            if (!stateMapExists) {
                arrayIndexMap = new int[maxArrays];
            }

            arrayIndexMap[index] = 0;
        } else {
            arrayIndexMap[index]++;
        }

        boolean hasNext = reader.hasNext();
        if (!hasNext) {
            reader.endArray();
            arrayIndexMap[index] = ARRAY_INDEX_NOT_SET;
        }
        return hasNext;
    }

    public final int getArrayIndex(int index) {
        return arrayIndexMap[index];
    }

    public final void onArrayFieldNotFound() throws IOException {
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