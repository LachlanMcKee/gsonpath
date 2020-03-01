package gsonpath

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import gsonpath.internal.GsonResultListTypeAdapterFactory

/**
 * Adds serialization/deserialization handling for GsonPath specific types to Gson.
 *
 * This version of the class proxies to GsonPathTypeAdapterFactory, so it can be used as the sole factory.
 */
class GsonPathTypeAdapterFactoryKt : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return gsonPathFactory.create(gson, type) ?: gsonResultListFactory.create(gson, type)
    }

    private val gsonPathFactory by lazy { GsonPathTypeAdapterFactory() }
    private val gsonResultListFactory by lazy { GsonResultListTypeAdapterFactory() }
}