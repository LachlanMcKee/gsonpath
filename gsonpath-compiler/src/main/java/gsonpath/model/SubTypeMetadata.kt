package gsonpath.model

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

/**
 * The type of value used when determining the correct subtype
 */
enum class SubTypeKeyType {
    STRING, INTEGER, BOOLEAN
}

/**
 * A data class that is used to convert the annotation 'stringValueSubtypes' 'booleanValueSubtypes' and 'integerValueSubtypes'
 * into a common reusable structure.
 */
data class GsonSubTypeKeyAndClass(val key: String, val clazzTypeMirror: TypeMirror)
