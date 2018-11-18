package gsonpath.model

import java.util.*

sealed class MutableGsonModel

data class MutableGsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean,
        val subTypeMetadata: SubTypeMetadata?) : MutableGsonModel() {

    fun toImmutable(): GsonField {
        return GsonField(
                fieldIndex = fieldIndex,
                fieldInfo = fieldInfo,
                variableName = variableName,
                jsonPath = jsonPath,
                isRequired = isRequired,
                subTypeMetadata = subTypeMetadata
        )
    }
}

data class MutableGsonObject(
        private val fieldMap: LinkedHashMap<String, MutableGsonModel> = LinkedHashMap()) : MutableGsonModel() {

    fun addObject(branchName: String, gsonObject: MutableGsonObject): MutableGsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addField(branchName: String, field: MutableGsonField): MutableGsonField {
        if (fieldMap.containsKey(branchName)) {
            throw IllegalArgumentException("Value already exists")
        }
        fieldMap[branchName] = field
        return field
    }

    operator fun get(key: String): MutableGsonModel? {
        return fieldMap[key]
    }

    fun toImmutable(): GsonObject {
        return GsonObject(fieldMap.entries
                .asSequence()
                .map { (key, value) ->
                    when (value) {
                        is MutableGsonField -> key to value.toImmutable()
                        is MutableGsonObject -> key to value.toImmutable()
                    }
                }
                .toMap())
    }
}
