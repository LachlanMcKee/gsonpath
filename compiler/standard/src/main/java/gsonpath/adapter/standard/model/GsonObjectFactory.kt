package gsonpath.adapter.standard.model

import gsonpath.GsonFieldValidationType
import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import java.util.regex.Pattern
import javax.lang.model.element.Element

class GsonObjectFactory(
        private val gsonObjectValidator: GsonObjectValidator,
        private val fieldPathFetcher: FieldPathFetcher) {

    @Throws(ProcessingException::class)
    fun addGsonType(
            gsonPathObject: MutableGsonObject,
            fieldInfo: FieldInfo,
            fieldInfoIndex: Int,
            metadata: GsonObjectMetadata) {

        val validationResult = gsonObjectValidator.validate(fieldInfo)

        val isPrimitive = fieldInfo.fieldType is FieldType.Primitive
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

        when (val jsonFieldPath = fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata)) {
            is FieldPath.Nested -> {
                addNestedType(gsonPathObject, fieldInfo, jsonFieldPath, metadata.flattenDelimiter,
                        fieldInfoIndex, isRequired)
            }

            is FieldPath.Standard -> {
                addStandardType(gsonPathObject, fieldInfo, jsonFieldPath,
                        fieldInfoIndex, isRequired)
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
            isRequired: Boolean) {

        // Ensure that the delimiter is correctly escaped before attempting to pathSegments the string.
        val regexSafeDelimiter: Regex = Pattern.quote(flattenDelimiter.toString()).toRegex()
        val pathSegments: List<String> = jsonFieldPath.path.split(regexSafeDelimiter)

        val lastPathIndex = pathSegments.size - 1
        val arrayIndexes = IntArray(pathSegments.size)

        (0..lastPathIndex).fold(gsonPathObject as MutableGsonModel) { currentModel: MutableGsonModel, index ->
            val pathType = getPathType(pathSegments[index])
            val pathKey = when (pathType) {
                is PathType.Array -> pathType.beforeArrayPath
                is PathType.Standard -> pathType.path
            }

            if (pathType is PathType.Array) {
                arrayIndexes[index] = pathType.index
            }

            val content = CommonSegmentContent(currentModel, arrayIndexes.toList(), index, fieldInfo, pathType, pathKey)
            if (index < lastPathIndex) {
                handleNestedSegment(content)

            } else {
                // We have reached the end of this object branch, add the field at the end.
                handleLastNestedSegment(content, fieldInfoIndex, jsonFieldPath, isRequired)
            }
        }
    }

    private fun handleNestedSegment(content: CommonSegmentContent): MutableGsonModel {
        val currentModel = content.currentModel
        val pathType = content.pathType
        val pathKey = content.pathKey

        return when (currentModel) {
            is MutableGsonObject -> {
                when (val existingGsonModel = currentModel[pathType.path]) {
                    null -> {
                        when (pathType) {
                            is PathType.Standard -> {
                                MutableGsonObject().also { newMap ->
                                    currentModel.addObject(pathType.path, newMap)
                                }
                            }
                            is PathType.Array -> {
                                currentModel.addArray(pathKey)
                            }
                        }
                    }
                    is MutableGsonObject -> {
                        existingGsonModel
                    }
                    is MutableGsonArray, is MutableGsonField -> {
                        // If this value already exists, and it is not a tree branch,
                        // that means we have an invalid duplicate.
                        throw ProcessingException("Unexpected duplicate field '" + pathType.path +
                                "' found. Each tree branch must use a unique value!", content.fieldInfo.element)
                    }
                }
            }
            is MutableGsonArray -> {
                // Now that it is established that the array contains an object, we add a container object.
                val previousArrayIndex = content.arrayIndexes[content.index - 1]
                val currentGsonType = currentModel[previousArrayIndex]
                if (currentGsonType == null) {
                    val gsonObject = currentModel.getObjectAtIndex(previousArrayIndex)
                    gsonObject.addObject(pathKey, MutableGsonObject())
                } else {
                    (currentGsonType as MutableGsonObject)[pathKey]!!
                }

            }
            else -> {
                throw ProcessingException("This should not happen!", content.fieldInfo.element)
            }
        }
    }

    private fun handleLastNestedSegment(
            content: CommonSegmentContent,
            fieldIndex: Int,
            jsonFieldPath: FieldPath.Nested,
            isRequired: Boolean): MutableGsonField = try {

        val parentModel = content.currentModel
        val pathType = content.pathType
        val pathKey = content.pathKey

        val finalModel =
                if (parentModel is MutableGsonArray) {
                    val previousArrayIndex = content.arrayIndexes[content.index - 1]
                    parentModel.getObjectAtIndex(previousArrayIndex)
                } else {
                    parentModel as MutableGsonObject
                }

        createField(fieldIndex, content.fieldInfo, jsonFieldPath.path, isRequired)
                .also { field ->
                    when (pathType) {
                        is PathType.Standard -> {
                            finalModel.addField(pathType.path, field)
                        }
                        is PathType.Array -> {
                            val gsonArray = finalModel.addArray(pathKey)
                            gsonArray.addField(content.arrayIndexes[content.index], field)
                        }
                    }
                }

    } catch (e: IllegalArgumentException) {
        throw ProcessingException("Unexpected duplicate field '" + content.pathType.path +
                "' found. Each tree branch must use a unique value!", content.fieldInfo.element)
    }

    @Throws(ProcessingException::class)
    private fun addStandardType(
            gsonPathObject: MutableGsonObject,
            fieldInfo: FieldInfo,
            jsonFieldPath: FieldPath.Standard,
            fieldInfoIndex: Int,
            isRequired: Boolean) {

        val pathType = getPathType(jsonFieldPath.path)
        val field = createField(fieldInfoIndex, fieldInfo, pathType.path, isRequired)

        when (pathType) {
            is PathType.Standard -> {
                if (gsonPathObject[pathType.path] == null) {
                    gsonPathObject.addField(pathType.path, field)
                } else {
                    throwDuplicateFieldException(fieldInfo.element, pathType.path)
                }
            }
            is PathType.Array -> {
                val gsonArray = gsonPathObject.addArray(pathType.beforeArrayPath)
                gsonArray.addField(pathType.index, field)
            }
        }
    }

    private fun createField(
            fieldIndex: Int,
            fieldInfo: FieldInfo,
            jsonPath: String,
            isRequired: Boolean): MutableGsonField {

        val variableName = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
        return MutableGsonField(fieldIndex, fieldInfo, variableName, jsonPath, isRequired)
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

    private data class CommonSegmentContent(
            val currentModel: MutableGsonModel,
            val arrayIndexes: List<Int>,
            val index: Int,
            val fieldInfo: FieldInfo,
            val pathType: PathType,
            val pathKey: String)

    private sealed class PathType(open val path: String) {
        data class Standard(override val path: String) : PathType(path)
        data class Array(override val path: String, val beforeArrayPath: String, val index: Int) : PathType(path)
    }
}