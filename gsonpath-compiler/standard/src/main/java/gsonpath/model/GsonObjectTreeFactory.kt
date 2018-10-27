package gsonpath.model

import gsonpath.ProcessingException
import java.util.regex.Pattern

class GsonObjectTreeFactory(private val gsonObjectFactory: GsonObjectFactory) {
    @Throws(ProcessingException::class)
    fun createGsonObject(
            fieldInfoList: List<FieldInfo>,
            rootField: String,
            metadata: GsonObjectMetadata): GsonObject {

        // Obtain the correct mapping structure beforehand.
        val absoluteRootObject = GsonObject()

        val gsonPathObject =
                if (rootField.isNotEmpty()) {
                    createGsonObjectFromRootField(absoluteRootObject, rootField, metadata.flattenDelimiter)

                } else {
                    absoluteRootObject
                }

        for (fieldInfoIndex in fieldInfoList.indices) {
            gsonObjectFactory.addGsonType(gsonPathObject, fieldInfoList[fieldInfoIndex], fieldInfoIndex, metadata)
        }
        return absoluteRootObject
    }

    private fun createGsonObjectFromRootField(
            rootObject: GsonObject,
            rootField: String,
            flattenDelimiter: Char): GsonObject {

        if (rootField.isEmpty()) {
            return rootObject
        }

        val regexSafeDelimiter = Pattern.quote(flattenDelimiter.toString())
        val split = rootField.split(regexSafeDelimiter.toRegex()).dropLastWhile(String::isEmpty)

        if (split.isNotEmpty()) {
            // Keep adding branches to the tree and switching our root to the new branch.
            return split.fold(rootObject) { currentRoot, field ->
                val currentObject = GsonObject()
                currentRoot.addObject(field, currentObject)
                return@fold currentObject
            }

        } else {
            // Add a single branch to the tree and return the new branch.
            val mapWithRoot = GsonObject()
            rootObject.addObject(rootField, mapWithRoot)
            return mapWithRoot
        }
    }

    fun getFlattenedFieldsFromGsonObject(gsonObject: GsonObject): List<GsonField> {
        return getFlattenedFields(gsonObject)
                .sortedBy { it.fieldIndex }
    }

    private fun getFlattenedFields(currentGsonObject: GsonObject): List<GsonField> {
        return currentGsonObject.entries()
                .map { it.value }
                .flatMap { gsonType ->
                    when (gsonType) {
                        is GsonField -> listOf(gsonType)

                        is GsonObject -> {
                            if (gsonType.size() > 0) {
                                getFlattenedFields(gsonType)
                            } else {
                                emptyList()
                            }
                        }
                    }
                }
    }
}
