package gsonpath.adapter.standard.model

class MandatoryFieldInfoFactory {

    /**
     * Keeps track of mandatory json field metadata.
     */
    class MandatoryFieldInfo(
            val indexVariableName: String,
            val gsonField: GsonField)

    /**
     * Add any mandatory field indexes as constants. This is done for code readability.
     * We will obtain the values using a depth-first recursion.
     */
    fun createMandatoryFieldsFromGsonObject(gsonObject: GsonObject): Map<String, MandatoryFieldInfo> {
        return gsonObject.entries()
                .fold(emptyMap()) { map, (_, gsonModel) ->
                    when (gsonModel) {
                        is GsonField -> handleField(gsonModel, map)
                        is GsonObject -> map.plus(createMandatoryFieldsFromGsonObject(gsonModel))
                        is GsonArray -> handleArray(gsonModel, map)
                    }
                }
    }

    /**
     * For all required fields we add an index field so we can easily check whether the
     * value has been assigned after the json has been parsed.
     */
    private fun handleField(
            gsonModel: GsonField,
            map: Map<String, MandatoryFieldInfo>): Map<String, MandatoryFieldInfo> {

        return when {
            gsonModel.isRequired -> {
                val fieldName = gsonModel.fieldInfo.fieldName
                val mandatoryFieldIndexName = "MANDATORY_INDEX_" + fieldName.toUpperCase()

                // Keep track of the information for later use. Since this is a linked list, we keep track of insert order.
                map.plus(Pair(fieldName, MandatoryFieldInfo(mandatoryFieldIndexName, gsonModel)))
            }
            else -> map
        }
    }

    private fun handleArray(
            arrayModel: GsonArray,
            map: Map<String, MandatoryFieldInfo>): Map<String, MandatoryFieldInfo> {

        return arrayModel.entries()
                .fold(map) { arrayMap, (_, arrayGsonModel) ->
                    when (arrayGsonModel) {
                        is GsonField -> handleField(arrayGsonModel, arrayMap)
                        is GsonObject -> arrayMap.plus(createMandatoryFieldsFromGsonObject(arrayGsonModel))
                    }
                }
    }
}
