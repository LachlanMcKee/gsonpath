package gsonpath.model

import java.util.*

sealed class GsonModel

data class GsonField(val fieldIndex: Int, val fieldInfo: FieldInfo, val jsonPath: String, val isRequired: Boolean) : GsonModel() {
    val variableName: String
        get() = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
}

data class GsonObject(private val fieldMap: LinkedHashMap<String, GsonModel> = LinkedHashMap()) : GsonModel() {

    fun addObject(branchName: String, gsonObject: GsonObject): GsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addField(branchName: String, field: GsonField): GsonField {
        if (fieldMap.containsKey(branchName)) {
            throw IllegalArgumentException("Value already exists")
        }
        fieldMap[branchName] = field
        return field
    }

    fun entries(): Set<Map.Entry<String, GsonModel>> {
        return fieldMap.entries
    }

    fun size(): Int {
        return fieldMap.size
    }

    operator fun get(key: String): GsonModel? {
        return fieldMap[key]
    }

    fun containsKey(key: String): Boolean {
        return fieldMap.containsKey(key)
    }
}