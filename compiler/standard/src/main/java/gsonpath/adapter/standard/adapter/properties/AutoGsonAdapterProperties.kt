package gsonpath.adapter.standard.adapter.properties

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution

data class AutoGsonAdapterProperties(
        val fieldsRequireAnnotation: Boolean,
        val flattenDelimiter: Char,
        val serializeNulls: Boolean,
        val rootField: String,
        val gsonFieldValidationType: GsonFieldValidationType,
        val gsonFieldNamingPolicy: FieldNamingPolicy,
        val pathSubstitutions: List<PathSubstitution>)
