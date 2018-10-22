package gsonpath.model

import gsonpath.GsonFieldValidationType
import gsonpath.ProcessingException
import java.util.regex.Pattern
import javax.lang.model.element.Element

class GsonObjectFactory(
        private val gsonObjectValidator: GsonObjectValidator,
        private val fieldPathFetcher: FieldPathFetcher,
        private val subTypeMetadataFactory: SubTypeMetadataFactory) {

    @Throws(ProcessingException::class)
    fun addGsonType(
            gsonPathObject: MutableGsonObject,
            fieldInfo: FieldInfo,
            fieldInfoIndex: Int,
            metadata: GsonObjectMetadata) {

        val validationResult = gsonObjectValidator.validate(fieldInfo)

        val isPrimitive = fieldInfo.typeName.isPrimitive
        val isRequired = when {
            validationResult == GsonObjectValidator.Result.Optional ->
                // Optionals will never fail regardless of the policy.
                false

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE ->
                // Using this policy everything is mandatory except for optionals.
                !fieldInfo.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL && isPrimitive ->
                // Primitives are treated as non-null implicitly.
                !fieldInfo.hasDefaultValue

            metadata.gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION ->
                false

            else ->
                validationResult == GsonObjectValidator.Result.Mandatory && !fieldInfo.hasDefaultValue
        }

        val gsonSubTypeMetadata = subTypeMetadataFactory.getGsonSubType(fieldInfo)

        val jsonFieldPath = fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata)
        when (jsonFieldPath) {
            is FieldPath.Nested -> {
                addNestedType(gsonPathObject, fieldInfo, jsonFieldPath, metadata.flattenDelimiter,
                        fieldInfoIndex, isRequired, gsonSubTypeMetadata)
            }

            is FieldPath.Standard -> {
                addStandardType(gsonPathObject, fieldInfo, jsonFieldPath,
                        fieldInfoIndex, isRequired, gsonSubTypeMetadata)
            }
        }
    }

    @Throws(ProcessingException::class)
    private fun addNestedType(
            gsonPathObject: MutableGsonObject,
            fieldInfo: FieldInfo,
            jsonFieldPath: FieldPath.Nested,
            flattenDelimiter: Char,
            fieldInfoIndex: Int,
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?) {

        // Ensure that the delimiter is correctly escaped before attempting to pathSegments the string.
        val regexSafeDelimiter: Regex = Pattern.quote(flattenDelimiter.toString()).toRegex()
        val pathSegments: List<String> = jsonFieldPath.path.split(regexSafeDelimiter)

        val lastPathIndex = pathSegments.size - 1
        val arrayIndexes = IntArray(pathSegments.size)

        (0..lastPathIndex).fold(gsonPathObject as MutableGsonModel) { current: MutableGsonModel, index ->
            val pathSegment = pathSegments[index]

            val isCurrentSegmentArray = pathSegment.contains("[")
            val pathKey =
                    if (isCurrentSegmentArray) {
                        pathSegment.substring(0, pathSegment.indexOf("["))
                    } else {
                        pathSegment
                    }
            if (isCurrentSegmentArray) {
                val arrayIndexString = pathSegment.substring(pathSegment.indexOf("[") + 1, pathSegment.indexOf("]"))
                arrayIndexes[index] = Integer.parseInt(arrayIndexString)
            }

            if (index < lastPathIndex) {

                if (current is MutableGsonObject) {
                    val gsonType = current[pathSegment]

                    if (gsonType != null) {
                        if (gsonType is MutableGsonObject) {
                            return@fold gsonType

                        } else {
                            // If this value already exists, and it is not a tree branch, that means we have an invalid duplicate.
                            throw ProcessingException("Unexpected duplicate field '" + pathSegment +
                                    "' found. Each tree branch must use a unique value!", fieldInfo.element)
                        }
                    } else {
                        if (isCurrentSegmentArray) {
                            return@fold current.addArray(pathKey)
                        } else {
                            val newMap = MutableGsonObject()
                            current.addObject(pathSegment, newMap)
                            return@fold newMap
                        }
                    }
                } else if (current is MutableGsonArray) {
                    // Now that it is established that the array contains an object, we add a container object.
                    val previousArrayIndex = arrayIndexes[index - 1]
                    val currentGsonType = current[previousArrayIndex]
                    if (currentGsonType == null) {
                        val gsonObject = current.getObjectAtIndex(previousArrayIndex)
                        return@fold gsonObject.addObject(pathKey, MutableGsonObject())
                    } else {
                        return@fold (currentGsonType as MutableGsonObject)[pathKey]!!
                    }

                } else {
                    throw ProcessingException("This should not happen!", fieldInfo.element)
                }

            } else {
                // We have reached the end of this object branch, add the field at the end.
                try {
                    val field = MutableGsonField(fieldInfoIndex, fieldInfo, getVariableName(jsonFieldPath.path), jsonFieldPath.path, isRequired, gsonSubTypeMetadata)

                    val temp =
                            if (current is MutableGsonArray) {
                                val previousArrayIndex = arrayIndexes[index - 1]
                                current.getObjectAtIndex(previousArrayIndex)
                            } else {
                                current
                            }
                    if (isCurrentSegmentArray) {
                        val gsonArray = (temp as MutableGsonObject).addArray(pathKey)
                        gsonArray.addField(arrayIndexes[index], field)
                    } else {
                        (temp as MutableGsonObject).addField(pathSegment, field)
                    }
                    return@fold field

                } catch (e: IllegalArgumentException) {
                    throw ProcessingException("Unexpected duplicate field '" + pathSegment +
                            "' found. Each tree branch must use a unique value!", fieldInfo.element)
                }

            }
        }
    }

    @Throws(ProcessingException::class)
    private fun addStandardType(
            gsonPathObject: MutableGsonObject,
            fieldInfo: FieldInfo,
            jsonFieldPath: FieldPath.Standard,
            fieldInfoIndex: Int,
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?) {

        val path = jsonFieldPath.path

        val isArray = path.contains("[")
        val arrayIndex: Int
        when {
            isArray -> {
                val nonArrayKey = path.substring(0, path.indexOf("["))
                arrayIndex = Integer.parseInt(path.substring(path.indexOf("[") + 1, path.indexOf("]")))
                val gsonArray = gsonPathObject.addArray(nonArrayKey)
                gsonArray.addField(arrayIndex, MutableGsonField(fieldInfoIndex, fieldInfo, getVariableName(path),
                        path, isRequired, gsonSubTypeMetadata))

            }
            gsonPathObject[path] == null -> {
                gsonPathObject.addField(path, MutableGsonField(fieldInfoIndex, fieldInfo, getVariableName(path),
                        path, isRequired, gsonSubTypeMetadata))
            }
            else -> throwDuplicateFieldException(fieldInfo.element, path)
        }
    }

    private fun getVariableName(jsonPath: String): String {
        return "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
    }

    @Throws(ProcessingException::class)
    private fun throwDuplicateFieldException(field: Element?, jsonKey: String) {
        throw ProcessingException("Unexpected duplicate field '" + jsonKey +
                "' found. Each tree branch must use a unique value!", field)
    }
}