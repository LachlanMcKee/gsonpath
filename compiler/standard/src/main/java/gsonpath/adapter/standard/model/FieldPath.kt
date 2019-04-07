package gsonpath.adapter.standard.model

sealed class FieldPath {
    data class Standard(val path: String) : FieldPath()
    data class Nested(val path: String) : FieldPath()
}