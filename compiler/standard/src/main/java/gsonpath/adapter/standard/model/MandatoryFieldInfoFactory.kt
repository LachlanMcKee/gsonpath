package gsonpath.adapter.standard.model

class MandatoryFieldInfoFactory {

    /**
     * Add any mandatory field indexes as constants. This is done for code readability.
     * We will obtain the values using a depth-first recursion.
     */
    fun createMandatoryFieldsFromGsonObject(gsonObject: GsonObject): List<GsonField> {
        return gsonObject.entries()
                .flatMap { (_, gsonModel) ->
                    when (gsonModel) {
                        is GsonField -> handleField(gsonModel)
                        is GsonObject -> createMandatoryFieldsFromGsonObject(gsonModel)
                        is GsonArray -> {
                            gsonModel.entries()
                                    .flatMap { (_, arrayGsonModel) ->
                                        when (arrayGsonModel) {
                                            is GsonField -> handleField(arrayGsonModel)
                                            is GsonObject -> createMandatoryFieldsFromGsonObject(arrayGsonModel)
                                        }
                                    }
                        }
                    }
                }
    }

    private fun handleField(gsonField: GsonField): List<GsonField> {
        return when {
            gsonField.isRequired -> listOf(gsonField)
            else -> emptyList()
        }
    }
}
