package gsonpath.result

/**
 * Used to keep track of succesful and unsucessful deserializations.
 */
sealed class GsonResult<T> {
    data class Success<T>(val value: T) : GsonResult<T>()
    data class Failure<T>(val exception: Exception) : GsonResult<T>()
}