package gsonpath.adapter.standard.model

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.annotation.PathSubstitution

data class GsonObjectMetadata(
        val flattenDelimiter: Char,
        val gsonFieldNamingPolicy: FieldNamingPolicy,
        val gsonFieldValidationType: GsonFieldValidationType,
        val pathSubstitutions: List<PathSubstitution>)