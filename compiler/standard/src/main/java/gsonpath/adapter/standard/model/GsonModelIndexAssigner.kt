package gsonpath.adapter.standard.model

object GsonModelIndexAssigner {

    fun assignObjectIndexes(gsonObject: GsonObject): List<GsonObject> {
        val results = gsonObject.entries()
                .flatMap { (_, gsonType) ->
                    when (gsonType) {
                        is GsonField -> emptyList()
                        is GsonObject -> assignObjectIndexes(gsonType)
                        is GsonArray -> {
                            gsonType.entries()
                                    .flatMap { (_, arrayGsonType) ->
                                        when (arrayGsonType) {
                                            is GsonField -> emptyList()
                                            is GsonObject -> assignObjectIndexes(arrayGsonType)
                                        }
                                    }
                        }
                    }
                }

        return listOf(gsonObject).plus(results)
    }

    fun assignArrayIndexes(gsonObject: GsonObject): List<GsonArray> {
        return gsonObject.entries()
                .flatMap { (_, gsonType) ->
                    when (gsonType) {
                        is GsonField -> emptyList()
                        is GsonObject -> assignArrayIndexes(gsonType)
                        is GsonArray -> {
                            val results = gsonType.entries()
                                    .flatMap { (_, arrayGsonType) ->
                                        when (arrayGsonType) {
                                            is GsonField -> emptyList()
                                            is GsonObject -> assignArrayIndexes(arrayGsonType)
                                        }
                                    }

                            listOf(gsonType).plus(results)
                        }
                    }
                }
    }

}