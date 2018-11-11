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

        when (val jsonFieldPath = fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata)) {
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
            val pathType = getPathType(pathSegments[index])
            val pathKey = when (pathType) {
                is PathType.Array -> pathType.beforeArrayPath
                is PathType.Standard -> pathType.path
            }

            if (pathType is PathType.Array) {
                arrayIndexes[index] = pathType.index
            }

            if (index < lastPathIndex) {

                if (current is MutableGsonObject) {
                    val gsonType = current[pathType.path]

                    if (gsonType != null) {
                        if (gsonType is MutableGsonObject) {
                            return@fold gsonType

                        } else {
                            // If this value already exists, and it is not a tree branch, that means we have an invalid duplicate.
                            throw ProcessingException("Unexpected duplicate field '" + pathType.path +
                                    "' found. Each tree branch must use a unique value!", fieldInfo.element)
                        }
                    } else {
                        when (pathType) {
                            is PathType.Standard -> {
                                val newMap = MutableGsonObject()
                                current.addObject(pathType.path, newMap)
                                return@fold newMap
                            }
                            is PathType.Array -> {
                                return@fold current.addArray(pathKey)
                            }
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
                    val finalObject =
                            if (current is MutableGsonArray) {
                                val previousArrayIndex = arrayIndexes[index - 1]
                                current.getObjectAtIndex(previousArrayIndex)
                            } else {
                                current as MutableGsonObject
                            }

                    val field = MutableGsonField(fieldInfoIndex, fieldInfo, getVariableName(jsonFieldPath.path), jsonFieldPath.path, isRequired, gsonSubTypeMetadata)

                    when (pathType) {
                        is PathType.Standard -> {
                            finalObject.addField(pathType.path, field)
                        }
                        is PathType.Array -> {
                            val gsonArray = finalObject.addArray(pathKey)
                            gsonArray.addField(arrayIndexes[index], field)
                        }
                    }
                    return@fold field

                } catch (e: IllegalArgumentException) {
                    throw ProcessingException("Unexpected duplicate field '" + pathType.path +
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

        when (val pathType = getPathType(jsonFieldPath.path)) {
            is PathType.Standard -> {
                if (gsonPathObject[pathType.path] == null) {
                    gsonPathObject.addField(pathType.path, MutableGsonField(fieldInfoIndex, fieldInfo,
                            getVariableName(pathType.path), pathType.path, isRequired, gsonSubTypeMetadata))
                } else {
                    throwDuplicateFieldException(fieldInfo.element, pathType.path)
                }
            }
            is PathType.Array -> {
                val gsonArray = gsonPathObject.addArray(pathType.beforeArrayPath)
                gsonArray.addField(pathType.index, MutableGsonField(fieldInfoIndex, fieldInfo,
                        getVariableName(pathType.path), pathType.path, isRequired, gsonSubTypeMetadata))
            }
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

    private fun getPathType(path: String): PathType {
        val arrayStartIndex = path.indexOf("[")
        return if (arrayStartIndex >= 0) {
            val arrayIndex = Integer.parseInt(path.substring(arrayStartIndex + 1, path.indexOf("]")))
            PathType.Array(path, path.substring(0, arrayStartIndex), arrayIndex)
        } else {
            PathType.Standard(path)
        }
    }

    sealed class PathType(open val path: String) {
        data class Standard(override val path: String) : PathType(path)
        data class Array(override val path: String, val beforeArrayPath: String, val index: Int) : PathType(path)
    }
}