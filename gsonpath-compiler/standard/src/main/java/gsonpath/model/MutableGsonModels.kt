package gsonpath.model

import java.util.*

sealed class MutableGsonModel
sealed class MutableGsonArrayElement : MutableGsonModel()

data class MutableGsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean,
        val subTypeMetadata: SubTypeMetadata?) : MutableGsonArrayElement()

data class MutableGsonObject(
        private val fieldMap: LinkedHashMap<String, MutableGsonModel> = LinkedHashMap()) : MutableGsonArrayElement() {

    fun addObject(branchName: String, gsonObject: MutableGsonObject): MutableGsonObject {
        fieldMap[branchName] = gsonObject
        return gsonObject
    }

    @Throws(IllegalArgumentException::class)
    fun addArray(branchName: String): MutableGsonArray {
        val array = fieldMap[branchName] as MutableGsonArray?
        if (array != null) {
            return array
        }

        val newArray = MutableGsonArray()
        fieldMap[branchName] = newArray
        return newArray
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

data class MutableGsonArray(
        private val arrayFields: MutableMap<Int, MutableGsonArrayElement> = HashMap()) : MutableGsonModel() {

    @Throws(IllegalArgumentException::class)
    fun addField(arrayIndex: Int, field: MutableGsonField) {
        if (containsKey(arrayIndex)) {
            throw IllegalArgumentException("Value already exists")
        }
        arrayFields[arrayIndex] = field
    }

    @Throws(IllegalArgumentException::class)
    fun getObjectAtIndex(arrayIndex: Int): MutableGsonObject {
        val gsonObject = arrayFields[arrayIndex] as MutableGsonObject?
        if (gsonObject != null) {
            return gsonObject
        }

        val newGsonObject = MutableGsonObject()
        arrayFields[arrayIndex] = newGsonObject
        return newGsonObject
    }

    fun entries(): Set<Map.Entry<Int, MutableGsonArrayElement>> {
        return arrayFields.entries
    }

    operator fun get(arrayIndex: Int): Any? {
        return arrayFields[arrayIndex]
    }

    fun containsKey(arrayIndex: Int?): Boolean {
        return arrayFields.containsKey(arrayIndex)
    }
}