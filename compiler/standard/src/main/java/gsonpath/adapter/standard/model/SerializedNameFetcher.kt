package gsonpath.adapter.standard.model

import com.google.gson.annotations.SerializedName
import gsonpath.NestedJson
import gsonpath.ProcessingException
import gsonpath.model.FieldInfo

object SerializedNameFetcher {
    fun getSerializedName(fieldInfo: FieldInfo, flattenDelimiter: Char): String? {
        val serializedNameAnnotation = fieldInfo.getAnnotation(SerializedName::class.java)
        val nestedJson = fieldInfo.getAnnotation(NestedJson::class.java)

        // SerializedName 'alternate' is not supported and should fail fast.
        serializedNameAnnotation?.let {
            if (it.alternate.isNotEmpty()) {
                throw ProcessingException("SerializedName 'alternate' feature is not supported", fieldInfo.element)
            }
        }

        nestedJson?.let {
            if (it.value.endsWith(flattenDelimiter)) {
                throw ProcessingException("NestedJson path must not end with '$flattenDelimiter'", fieldInfo.element)
            }
        }

        return when {
            nestedJson != null && serializedNameAnnotation != null -> {
                nestedJson.value + flattenDelimiter + serializedNameAnnotation.value
            }
            nestedJson != null -> nestedJson.value + flattenDelimiter
            serializedNameAnnotation != null -> serializedNameAnnotation.value
            else -> null
        }
    }
}