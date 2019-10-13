package gsonpath.result

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gsonpath.GsonUtil.isValidValue
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
        return Adapter(elementTypeAdapter) as TypeAdapter<T>
    }

    private class Adapter<E>(private val elementTypeAdapter: TypeAdapter<E>) : TypeAdapter<GsonResultList<E>>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): GsonResultList<E>? {
            if (!isValidValue(reader)) {
                return null
            }

            return GsonResultList(Streams.parse(reader)
                    .asJsonArray
                    .map { jsonElement ->
                        try {
                            GsonResult.Success(elementTypeAdapter.fromJsonTree(jsonElement))
                        } catch (e: Exception) {
                            GsonResult.Failure<E>(e)
                        }
                    })
        }

        @Throws(IOException::class)
        override fun write(writer: JsonWriter, collection: GsonResultList<E>) {
            throw JsonIOException("Writing is not supported by GsonResultList")
        }
    }
}