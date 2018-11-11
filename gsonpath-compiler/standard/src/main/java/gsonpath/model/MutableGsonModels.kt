package gsonpath.model

import java.util.*

sealed class MutableGsonModel

data class MutableGsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean,
        val subTypeMetadata: SubTypeMetadata?) : MutableGsonModel()

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

    fun entries(): Set<Map.Entry<String, MutableGsonModel>> {
        return fieldMap.entries
    }

    operator fun get(key: String): MutableGsonModel? {
        return fieldMap[key]
    }
}