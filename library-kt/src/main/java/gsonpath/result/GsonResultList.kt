package gsonpath.result

/**
 * A list that stores valid and invalid results when parsing via Gson.
 *
 * All succesfully parsed results are wrapped with GsonResult.Success, and all results that
 * threw an exception are wrapped with Failure.Failure
 *
 * @param <T> the element type within the list
 */
class GsonResultList<T>(private val list: List<GsonResult<T>>) : List<GsonResult<T>> by list
