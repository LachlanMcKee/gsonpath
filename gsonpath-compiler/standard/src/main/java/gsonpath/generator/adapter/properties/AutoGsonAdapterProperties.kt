package gsonpath.generator.adapter.properties

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution

class AutoGsonAdapterProperties(
        val fieldsRequireAnnotation: Boolean,
        val flattenDelimiter: Char,
        val serializeNulls: Boolean,
        val rootField: String,
        val gsonFieldValidationType: GsonFieldValidationType,
        val gsonFieldNamingPolicy: FieldNamingPolicy,
        val pathSubstitutions: Array<PathSubstitution>)
