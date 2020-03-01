package gsonpath;

import java.util.ArrayList;

/**
 * A list that stores only valid results when parsing via Gson.
 * <p>
 * Any elements being deserialied that throw an exception are removed from the list.
 *
 * @param <T> the element type within the list
 */
public final class GsonSafeList<T> extends ArrayList<T> {
}
