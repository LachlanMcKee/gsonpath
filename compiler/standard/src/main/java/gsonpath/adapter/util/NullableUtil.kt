package gsonpath.adapter.util

class NullableUtil {
    fun isNullableKeyword(keyword: String): Boolean {
        return arrayOf("NonNull", "Nonnull", "NotNull", "Notnull").contains(keyword)
    }
}