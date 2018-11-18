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

        (0..lastPathIndex).fold(gsonPathObject as MutableGsonModel) { currentModel: MutableGsonModel, index ->
            val pathType = getPathType(pathSegments[index])

            val content = CommonSegmentContent(currentModel, index, fieldInfo, pathType)
            if (index < lastPathIndex) {
                handleNestedSegment(content)

            } else {
                // We have reached the end of this object branch, add the field at the end.
                handleLastNestedSegment(content, fieldInfoIndex, jsonFieldPath, isRequired, gsonSubTypeMetadata)
            }
        }
    }

    private fun handleNestedSegment(content: CommonSegmentContent): MutableGsonModel {
        val currentModel = content.currentModel
        val pathType = content.pathType

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
                        }
                    }
                    is MutableGsonObject -> {
                        existingGsonModel
                    }
                    is MutableGsonField -> {
                        // If this value already exists, and it is not a tree branch,
                        // that means we have an invalid duplicate.
                        throw ProcessingException("Unexpected duplicate field '" + pathType.path +
                                "' found. Each tree branch must use a unique value!", content.fieldInfo.element)
                    }
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
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?): MutableGsonField = try {

        val parentModel = content.currentModel
        val pathType = content.pathType

        val finalModel = parentModel as MutableGsonObject

        createField(fieldIndex, content.fieldInfo, jsonFieldPath.path, isRequired, gsonSubTypeMetadata)
                .also { field ->
                    when (pathType) {
                        is PathType.Standard -> {
                            finalModel.addField(pathType.path, field)
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
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?) {

        val pathType = getPathType(jsonFieldPath.path)
        val field = createField(fieldInfoIndex, fieldInfo, pathType.path, isRequired, gsonSubTypeMetadata)

        when (pathType) {
            is PathType.Standard -> {
                if (gsonPathObject[pathType.path] == null) {
                    gsonPathObject.addField(pathType.path, field)
                } else {
                    throwDuplicateFieldException(fieldInfo.element, pathType.path)
                }
            }
        }
    }

    private fun createField(
            fieldIndex: Int,
            fieldInfo: FieldInfo,
            jsonPath: String,
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?): MutableGsonField {

        val variableName = "value_" + jsonPath.replace("[^A-Za-z0-9_]".toRegex(), "_")
        return MutableGsonField(fieldIndex, fieldInfo, variableName, jsonPath, isRequired, gsonSubTypeMetadata)
    }

    @Throws(ProcessingException::class)
    private fun throwDuplicateFieldException(field: Element?, jsonKey: String) {
        throw ProcessingException("Unexpected duplicate field '" + jsonKey +
                "' found. Each tree branch must use a unique value!", field)
    }

    private fun getPathType(path: String): PathType {
        return PathType.Standard(path)
    }

    private data class CommonSegmentContent(
            val currentModel: MutableGsonModel,
            val index: Int,
            val fieldInfo: FieldInfo,
            val pathType: PathType)

    private sealed class PathType(open val path: String) {
        data class Standard(override val path: String) : PathType(path)
    }
}