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
            gsonPathObject: GsonObject,
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
            gsonPathObject: GsonObject,
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

        (0..lastPathIndex).fold(gsonPathObject as GsonModel) { current: GsonModel, index ->
            val pathSegment = pathSegments[index]

            if (index < lastPathIndex) {

                if (current is GsonObject) {
                    val gsonType = current[pathSegment]

                    if (gsonType != null) {
                        if (gsonType is GsonObject) {
                            return@fold gsonType

                        } else {
                            // If this value already exists, and it is not a tree branch, that means we have an invalid duplicate.
                            throw ProcessingException("Unexpected duplicate field '" + pathSegment +
                                    "' found. Each tree branch must use a unique value!", fieldInfo.element)
                        }
                    } else {
                        val newMap = GsonObject()
                        current.addObject(pathSegment, newMap)
                        return@fold newMap
                    }
                } else {
                    throw ProcessingException("This should not happen!", fieldInfo.element)
                }

            } else {
                // We have reached the end of this object branch, add the field at the end.
                try {
                    val field = GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath.path, isRequired, gsonSubTypeMetadata)
                    return@fold (current as GsonObject).addField(pathSegment, field)

                } catch (e: IllegalArgumentException) {
                    throw ProcessingException("Unexpected duplicate field '" + pathSegment +
                            "' found. Each tree branch must use a unique value!", fieldInfo.element)
                }

            }
        }
    }

    @Throws(ProcessingException::class)
    private fun addStandardType(
            gsonPathObject: GsonObject,
            fieldInfo: FieldInfo,
            jsonFieldPath: FieldPath.Standard,
            fieldInfoIndex: Int,
            isRequired: Boolean,
            gsonSubTypeMetadata: SubTypeMetadata?) {

        val path = jsonFieldPath.path
        if (!gsonPathObject.containsKey(path)) {
            gsonPathObject.addField(path, GsonField(fieldInfoIndex, fieldInfo, path, isRequired, gsonSubTypeMetadata))

        } else {
            throwDuplicateFieldException(fieldInfo.element, path)
        }
    }

    @Throws(ProcessingException::class)
    private fun throwDuplicateFieldException(field: Element?, jsonKey: String) {
        throw ProcessingException("Unexpected duplicate field '" + jsonKey +
                "' found. Each tree branch must use a unique value!", field)
    }
}