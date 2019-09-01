package gsonpath.adapter.util

object NullableUtil {
    fun isNullableKeyword(keyword: String): Boolean {
        return arrayOf("NonNull", "Nonnull", "NotNull", "Notnull").contains(keyword)
    }
}