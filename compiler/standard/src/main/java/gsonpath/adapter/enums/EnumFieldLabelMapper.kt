package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import java.util.*

object EnumFieldLabelMapper {
    fun map(fieldName: String, fieldNamingPolicy: FieldNamingPolicy): String {
        return when (fieldNamingPolicy) {
            FieldNamingPolicy.IDENTITY -> fieldName
            FieldNamingPolicy.UPPER_CAMEL_CASE -> mapUpperCamelCase(fieldName)
            FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES -> mapUpperCamelCaseWithSpaces(fieldName)
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES -> mapLowerCaseWithUnderscores(fieldName)
            FieldNamingPolicy.LOWER_CASE_WITH_DASHES -> mapLowerCaseWithDashes(fieldName)
        }
    }

    private fun map(fieldName: String, separator: String, transformFunc: (String) -> String = { it }): String {
        return fieldName.split("_")
                .filter { it.isNotBlank() }
                .joinToString(separator) { fieldSegment ->
                    fieldSegment
                            .toLowerCase(Locale.ENGLISH)
                            .let(transformFunc)
                }
    }

    private fun mapUpperCamelCase(fieldName: String): String {
        return map(fieldName, "") { it.capitalize() }
    }

    private fun mapUpperCamelCaseWithSpaces(fieldName: String): String {
        return map(fieldName, " ") { it.capitalize() }
    }

    private fun mapLowerCaseWithUnderscores(fieldName: String): String {
        return map(fieldName, "_")
    }

    private fun mapLowerCaseWithDashes(fieldName: String): String {
        return map(fieldName, "-")
    }
}
