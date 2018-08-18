package gsonpath.model

import com.google.gson.FieldNamingPolicy
import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.ProcessingException
import java.util.regex.Pattern
import javax.lang.model.element.Element

class GsonObjectFactory {
    @Throws(ProcessingException::class)
    fun addGsonType(gsonPathObject: GsonObject,
                    fieldInfo: FieldInfo,
                    fieldInfoIndex: Int,
                    flattenDelimiter: Char,
                    gsonFieldNamingPolicy: FieldNamingPolicy,
                    gsonFieldValidationType: GsonFieldValidationType,
                    pathSubstitutions: Array<PathSubstitution>) {

        val fieldTypeName = fieldInfo.typeName

        if (fieldTypeName == TypeName.OBJECT) {
            throw ProcessingException("Invalid field type: $fieldTypeName", fieldInfo.element)
        }

        val serializedNameAnnotation = fieldInfo.getAnnotation(SerializedName::class.java)

        // SerializedName 'alternate' is not supported and should fail fast.
        serializedNameAnnotation?.let {
            if (it.alternate.isNotEmpty()) {
                throw ProcessingException("SerializedName 'alternate' feature is not supported", fieldInfo.element)
            }
        }

        val fieldName = fieldInfo.fieldName
        val jsonFieldPath: String =
                if (serializedNameAnnotation != null && serializedNameAnnotation.value.isNotEmpty()) {
                    if (pathSubstitutions.isNotEmpty()) {

                        // Check if the serialized name needs any values to be substituted
                        pathSubstitutions.fold(serializedNameAnnotation.value) { fieldPath, substitution ->
                            fieldPath.replace("{${substitution.original}}", substitution.replacement)
                        }

                    } else {
                        serializedNameAnnotation.value
                    }

                } else {
                    // Since the serialized annotation wasn't specified, we need to apply the naming policy instead.
                    FieldNamingPolicyMapper.applyFieldNamingPolicy(gsonFieldNamingPolicy, fieldName)
                }

        // Attempt to find a Nullable or NonNull annotation type.
        val isOptional: Boolean = fieldInfo.annotationNames.any { it == "Nullable" }
        val isMandatory: Boolean = fieldInfo.annotationNames.any {
            arrayOf("NonNull", "Nonnull", "NotNull", "Notnull").contains(it)
        }

        // Fields cannot use both annotations.
        if (isMandatory && isOptional) {
            throw ProcessingException("Field cannot have both Mandatory and Optional annotations", fieldInfo.element)
        }

        // Primitives should not use either annotation.
        val isPrimitive = fieldTypeName.isPrimitive
        if (isPrimitive && (isMandatory || isOptional)) {
            throw ProcessingException("Primitives should not use NonNull or Nullable annotations", fieldInfo.element)
        }

        val isRequired = when {
            isOptional ->
                // Optionals will never fail regardless of the policy.
                false

            gsonFieldValidationType == GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE ->
                // Using this policy everything is mandatory except for optionals.
                !fieldInfo.hasDefaultValue

            gsonFieldValidationType == GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL && isPrimitive ->
                // Primitives are treated as non-null implicitly.
                !fieldInfo.hasDefaultValue

            gsonFieldValidationType == GsonFieldValidationType.NO_VALIDATION ->
                false

            else ->
                isMandatory && !fieldInfo.hasDefaultValue
        }

        if (jsonFieldPath.contains(flattenDelimiter.toString())) {
            addNestedType(gsonPathObject, fieldInfo, jsonFieldPath, flattenDelimiter, fieldInfoIndex, isRequired,
                    fieldName)

        } else {
            addStandardType(gsonPathObject, fieldInfo, jsonFieldPath, fieldInfoIndex, isRequired)
        }
    }

    @Throws(ProcessingException::class)
    private fun addNestedType(gsonPathObject: GsonObject,
                              fieldInfo: FieldInfo,
                              initialJsonFieldPath: String,
                              flattenDelimiter: Char,
                              fieldInfoIndex: Int,
                              isRequired: Boolean,
                              fieldName: String) {

        val jsonFieldPath =
                //
                // When the last character is a delimiter, we should append the variable name to
                // the end of the field name, as this may reduce annotation repetition.
                //
                if (initialJsonFieldPath[initialJsonFieldPath.length - 1] == flattenDelimiter) {
                    initialJsonFieldPath + fieldName
                } else {
                    initialJsonFieldPath
                }

        // Ensure that the delimiter is correctly escaped before attempting to pathSegments the string.
        val regexSafeDelimiter: Regex = Pattern.quote(flattenDelimiter.toString()).toRegex()
        val pathSegments: List<String> = jsonFieldPath.split(regexSafeDelimiter)

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
                    val field = GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired)
                    return@fold (current as GsonObject).addField(pathSegment, field)

                } catch (e: IllegalArgumentException) {
                    throw ProcessingException("Unexpected duplicate field '" + pathSegment +
                            "' found. Each tree branch must use a unique value!", fieldInfo.element)
                }

            }
        }
    }

    @Throws(ProcessingException::class)
    private fun addStandardType(gsonPathObject: GsonObject,
                                fieldInfo: FieldInfo,
                                jsonFieldPath: String,
                                fieldInfoIndex: Int,
                                isRequired: Boolean) {

        if (!gsonPathObject.containsKey(jsonFieldPath)) {
            gsonPathObject.addField(jsonFieldPath, GsonField(fieldInfoIndex, fieldInfo, jsonFieldPath, isRequired))

        } else {
            throwDuplicateFieldException(fieldInfo.element, jsonFieldPath)
        }
    }

    @Throws(ProcessingException::class)
    private fun throwDuplicateFieldException(field: Element?, jsonKey: String) {
        throw ProcessingException("Unexpected duplicate field '" + jsonKey +
                "' found. Each tree branch must use a unique value!", field)
    }
}
