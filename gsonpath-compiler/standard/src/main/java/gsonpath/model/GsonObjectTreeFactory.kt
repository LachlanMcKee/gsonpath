package gsonpath.model

import gsonpath.ProcessingException
import java.util.regex.Pattern

data class GsonTreeResult(
        val rootObject: GsonObject,
        val flattenedFields: List<GsonField>
)

class GsonObjectTreeFactory(private val gsonObjectFactory: GsonObjectFactory) {
    @Throws(ProcessingException::class)
    fun createGsonObject(
            fieldInfoList: List<FieldInfo>,
            rootField: String,
            metadata: GsonObjectMetadata): GsonTreeResult {

        // Obtain the correct mapping structure beforehand.
        val absoluteRootObject = MutableGsonObject()

        val gsonPathObject =
                if (rootField.isNotEmpty()) {
                    createGsonObjectFromRootField(absoluteRootObject, rootField, metadata.flattenDelimiter)

                } else {
                    absoluteRootObject
                }

        for (fieldInfoIndex in fieldInfoList.indices) {
            gsonObjectFactory.addGsonType(gsonPathObject, fieldInfoList[fieldInfoIndex], fieldInfoIndex, metadata)
        }
        val immutableAbsoluteGsonObject = absoluteRootObject.toImmutable()
        val flattenedFields = getFlattenedFields(immutableAbsoluteGsonObject).sortedBy { it.fieldIndex }
        return GsonTreeResult(immutableAbsoluteGsonObject, flattenedFields)
    }

    private fun createGsonObjectFromRootField(
            rootObject: MutableGsonObject,
            rootField: String,
            flattenDelimiter: Char): MutableGsonObject {

        if (rootField.isEmpty()) {
            return rootObject
        }

        val regexSafeDelimiter = Pattern.quote(flattenDelimiter.toString())
        val split = rootField.split(regexSafeDelimiter.toRegex()).dropLastWhile(String::isEmpty)

        if (split.isNotEmpty()) {
            // Keep adding branches to the tree and switching our root to the new branch.
            return split.fold(rootObject) { currentRoot, field ->
                val currentObject = MutableGsonObject()
                currentRoot.addObject(field, currentObject)
                return@fold currentObject
            }

        } else {
            // Add a single branch to the tree and return the new branch.
            val mapWithRoot = MutableGsonObject()
            rootObject.addObject(rootField, mapWithRoot)
            return mapWithRoot
        }
    }

    private fun getFlattenedFields(currentGsonObject: GsonObject): List<GsonField> {
        return currentGsonObject.entries()
                .flatMap { (_, gsonType) ->
                    when (gsonType) {
                        is GsonField -> listOf(gsonType)
                        is GsonObject -> getFlattenedFields(gsonType)
                        is GsonArray -> {
                            gsonType.entries()
                                    .map { it.value }
                                    .flatMap { arrayGsonType ->
                                        when (arrayGsonType) {
                                            is GsonField -> listOf(arrayGsonType)
                                            is GsonObject -> getFlattenedFields(arrayGsonType)
                                        }
                                    }
                        }
                    }
                }
    }

    private fun MutableGsonObject.toImmutable(): GsonObject {
        return GsonObject(entries()
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

    private fun MutableGsonField.toImmutable(): GsonField {
        return GsonField(
                fieldIndex = fieldIndex,
                fieldInfo = fieldInfo,
                variableName = variableName,
                jsonPath = jsonPath,
                isRequired = isRequired,
                subTypeMetadata = subTypeMetadata
        )
    }

    private fun MutableGsonArray.toImmutable(): GsonArray {
        return GsonArray(entries()
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
