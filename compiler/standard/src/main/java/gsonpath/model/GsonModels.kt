package gsonpath.model

sealed class GsonModel
sealed class GsonArrayElement : GsonModel()

data class GsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean) : GsonArrayElement()

data class GsonObject(private val fieldMap: Map<String, GsonModel>) : GsonArrayElement() {

    fun entries(): Set<Map.Entry<String, GsonModel>> {
        return fieldMap.entries
    }

    fun size(): Int {
        return fieldMap.size
    }
}

data class GsonArray(
        private val arrayFields: Map<Int, GsonArrayElement> = HashMap(),
        val maxIndex: Int) : GsonModel() {

    fun entries(): Set<Map.Entry<Int, GsonArrayElement>> {
        return arrayFields.entries
    }

    operator fun get(arrayIndex: Int): GsonArrayElement? {
        return arrayFields[arrayIndex]
    }
}