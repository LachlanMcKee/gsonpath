package gsonpath.model

sealed class FieldPath {
    class Standard(val path: String) : FieldPath()
    class Nested(val path: String) : FieldPath()
}