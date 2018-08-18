package gsonpath.generator.standard

import gsonpath.GsonSubTypeFailureOutcome
import javax.lang.model.type.TypeMirror

data class SubTypeMetadata(
        val className: String,
        val variableName: String,
        val getterName: String,
        val fieldName: String,
        val keyType: SubTypeKeyType,
        val gsonSubTypeKeys: List<GsonSubTypeKeyAndClass>,
        val defaultType: TypeMirror?,
        val failureOutcome: GsonSubTypeFailureOutcome)