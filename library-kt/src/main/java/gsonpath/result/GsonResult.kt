package gsonpath.result

import com.google.gson.JsonParseException

/**
 * Used to keep track of succesful and unsucessful deserializations.
 */
sealed class GsonResult<T> {
    data class Success<T>(val value: T) : GsonResult<T>()
    data class Failure<T>(val exception: JsonParseException) : GsonResult<T>()
}