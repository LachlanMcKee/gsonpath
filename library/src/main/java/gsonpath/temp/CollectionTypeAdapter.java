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

package gsonpath.temp;

import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.internal.GsonPathTypeAdapter;

import java.io.IOException;
import java.util.Collection;

/**
 * Adapt a homogeneous collection of objects.
 */
public final class CollectionTypeAdapter<E> extends GsonPathTypeAdapter<Collection<E>> {
    private final GsonPathTypeAdapter<E> elementTypeAdapter;
    private final ObjectConstructor<? extends Collection<E>> constructor;

    public CollectionTypeAdapter(Gson context,
                                 GsonPathTypeAdapter<E> elementTypeAdapter,
                                 ObjectConstructor<? extends Collection<E>> constructor) {

        super(context);
        this.elementTypeAdapter = elementTypeAdapter;
        this.constructor = constructor;
    }

    @Override
    public Collection<E>  readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Collection<E> collection = constructor.construct();
        in.beginArray();
        while (in.hasNext()) {
            E instance = elementTypeAdapter.read(in, gsonErrors);
            collection.add(instance);
        }
        in.endArray();
        return collection;
    }

    @Override
    public void writeImpl(JsonWriter out, Collection<E> collection) throws IOException {
        if (collection == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (E element : collection) {
            elementTypeAdapter.write(out, element);
        }
        out.endArray();
    }
}
