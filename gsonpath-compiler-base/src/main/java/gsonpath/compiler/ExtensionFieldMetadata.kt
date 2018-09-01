package gsonpath.compiler

import gsonpath.model.FieldInfo

/**
 * Metadata about the field being read by the parent processor
 */
data class ExtensionFieldMetadata(
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean)