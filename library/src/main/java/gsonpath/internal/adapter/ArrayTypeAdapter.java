/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gsonpath.internal.adapter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapt an array of objects.
 */
public final class ArrayTypeAdapter<E> extends GsonPathTypeAdapter<Object> {

    private final Class<E> componentType;
    private final GsonPathTypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson context, GsonPathTypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        super(context);
        this.componentTypeAdapter = componentTypeAdapter;
        this.componentType = componentType;
    }

    @Override
    public Object readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        List<E> list = new ArrayList<E>();
        in.beginArray();
        while (in.hasNext()) {
            E instance = componentTypeAdapter.read(in, gsonErrors);
            list.add(instance);
        }
        in.endArray();

        int size = list.size();
        Object array = Array.newInstance(componentType, size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeImpl(JsonWriter out, Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (int i = 0, length = Array.getLength(array); i < length; i++) {
            E value = (E) Array.get(array, i);
            componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
}
