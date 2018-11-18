package gsonpath.model

sealed class GsonModel

data class GsonField(
        val fieldIndex: Int,
        val fieldInfo: FieldInfo,
        val variableName: String,
        val jsonPath: String,
        val isRequired: Boolean,
        val subTypeMetadata: SubTypeMetadata?) : GsonModel()

data class GsonObject(private val fieldMap: Map<String, GsonModel>) : GsonModel() {

    fun entries(): Set<Map.Entry<String, GsonModel>> {
        return fieldMap.entries
    }

    fun size(): Int {
        return fieldMap.size
    }
}
