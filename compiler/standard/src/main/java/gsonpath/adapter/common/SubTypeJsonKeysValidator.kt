package gsonpath.adapter.common

import gsonpath.ProcessingException
import javax.lang.model.element.TypeElement

class SubTypeJsonKeysValidator {

    fun validateJsonKeys(classElement: TypeElement, jsonKeys: Array<String>): ProcessingException? {
        if (jsonKeys.isEmpty()) {
            return ProcessingException("At least one json key must be defined for GsonSubType", classElement)
        }

        if (jsonKeys.any { it.isBlank() }) {
            return ProcessingException("A blank json key is not valid for GsonSubType", classElement)
        }

        return jsonKeys.groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .keys
                .firstOrNull()
                ?.let {
                    throw ProcessingException("The json key '\"$it\"' appears more than once", classElement)
                }
    }
}