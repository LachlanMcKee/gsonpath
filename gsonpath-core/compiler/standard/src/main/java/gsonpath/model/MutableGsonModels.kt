package gsonpath.model

import gsonpath.ProcessingException
import java.util.*

sealed class MutableGsonModel
sealed class MutableGsonArrayElement : MutableGsonModel()

data class MutableGsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean) : MutableGsonArrayElement() {

    fun toImmutable(): GsonField {
        return GsonField(
                fieldIndex = fieldIndex,
                fieldInfo = fieldInfo,
                variableName = variableName,
                jsonPath = jsonPath,
                isRequired = isRequired
        )
    }
}

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
                        is MutableGsonArray -> key to value.toImmutable()
                    }
                }
                .toMap())
    }
}

data class MutableGsonArray(
        private val arrayFields: MutableMap<Int, MutableGsonArrayElement> = HashMap()) : MutableGsonModel() {

    @Throws(IllegalArgumentException::class)
    fun addField(arrayIndex: Int, field: MutableGsonField) {
        if (arrayFields.containsKey(arrayIndex)) {
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

    operator fun get(arrayIndex: Int): Any? {
        return arrayFields[arrayIndex]
    }

    fun toImmutable(): GsonArray {
        return arrayFields.entries.let { entries ->
            val maxIndex = entries.maxBy { it.key }?.key
                    ?: throw ProcessingException("Array should not be empty")

            GsonArray(
                    maxIndex = maxIndex,
                    arrayFields = entries
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
}