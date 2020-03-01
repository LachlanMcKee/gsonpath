package gsonpath.internal

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gsonpath.GsonErrors
import gsonpath.GsonResult
import gsonpath.GsonResultList
import gsonpath.internal.GsonUtil.isValidValue
import gsonpath.internal.adapter.GsonPathTypeAdapter
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * A factory for the list that stores valid and invalid results when parsing via Gson.
 */
internal class GsonResultListTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, typeToken: TypeToken<T>): TypeAdapter<T>? {
        if (GsonResultList::class.java != typeToken.rawType) {
            return null
        }

        val elementType = (typeToken.type as ParameterizedType).actualTypeArguments[0]
        val elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType))

        @Suppress("UNCHECKED_CAST")
        return Adapter(gson, elementTypeAdapter) as TypeAdapter<T>
    }

    private class Adapter<E>(
            gson: Gson,
            private val elementTypeAdapter: TypeAdapter<E>
    ) : GsonPathTypeAdapter<GsonResultList<E>>(gson) {

        @Throws(IOException::class)
        override fun readImpl(reader: JsonReader, gsonErrors: GsonErrors?): GsonResultList<E>? {
            if (!isValidValue(reader)) {
                return null
            }

            return GsonResultList(Streams.parse(reader)
                    .asJsonArray
                    .map { jsonElement ->
                        try {
                            GsonResult.Success(elementTypeAdapter.fromJsonTree(jsonElement))
                        } catch (e: JsonParseException) {
                            GsonResult.Failure<E>(e)
                        }
                    })
        }

        @Throws(IOException::class)
        override fun writeImpl(writer: JsonWriter, collection: GsonResultList<E>) {
            throw JsonIOException("Writing is not supported by GsonResultList")
        }
    }
}