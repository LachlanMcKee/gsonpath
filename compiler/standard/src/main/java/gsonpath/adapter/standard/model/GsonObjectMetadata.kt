package gsonpath.adapter.standard.model

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution

class GsonObjectMetadata(
        val flattenDelimiter: Char,
        val gsonFieldNamingPolicy: FieldNamingPolicy,
        val gsonFieldValidationType: GsonFieldValidationType,
        val pathSubstitutions: Array<PathSubstitution>)