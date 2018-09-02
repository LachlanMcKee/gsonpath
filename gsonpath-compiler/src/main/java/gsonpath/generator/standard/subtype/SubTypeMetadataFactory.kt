package gsonpath.generator.standard.subtype

import gsonpath.GsonSubTypeFailureOutcome
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.generator.standard.SharedFunctions
import gsonpath.model.FieldInfo
import gsonpath.util.TypeHandler
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

interface SubTypeMetadataFactory {
    fun getGsonSubType(fieldInfo: FieldInfo): SubTypeMetadata?
}

class SubTypeMetadataFactoryImpl(private val typeHandler: TypeHandler) : SubTypeMetadataFactory {

    override fun getGsonSubType(fieldInfo: FieldInfo): SubTypeMetadata? {
        return fieldInfo.getAnnotation(GsonSubtype::class.java)?.let {
            validateGsonSubType(fieldInfo, it)
        }
    }

    /**
     * Validates the GsonSubType annotation and returns a valid version that contains no incorrect data.
     * Any incorrect usages will cause an exception to be thrown.
     */
    private fun validateGsonSubType(fieldInfo: FieldInfo, gsonSubType: GsonSubtype): SubTypeMetadata {
        if (gsonSubType.subTypeKey.isBlank()) {
            throw ProcessingException("subTypeKey cannot be blank for GsonSubType", fieldInfo.element)
        }

        val keyCount =
                (if (gsonSubType.stringValueSubtypes.isNotEmpty()) 1 else 0) +
                        (if (gsonSubType.integerValueSubtypes.isNotEmpty()) 1 else 0) +
                        (if (gsonSubType.booleanValueSubtypes.isNotEmpty()) 1 else 0)

        if (keyCount > 1) {
            throw ProcessingException("Only one keys array (string, integer or boolean) may be specified for the GsonSubType",
                    fieldInfo.element)
        }

        val keyType: SubTypeKeyType =
                when {
                    gsonSubType.stringValueSubtypes.isNotEmpty() -> SubTypeKeyType.STRING
                    gsonSubType.integerValueSubtypes.isNotEmpty() -> SubTypeKeyType.INTEGER
                    gsonSubType.booleanValueSubtypes.isNotEmpty() -> SubTypeKeyType.BOOLEAN
                    else -> throw ProcessingException("Keys must be specified for the GsonSubType", fieldInfo.element)
                }

        //
        // Convert the provided keys into a unified type. Unfortunately due to how annotations work, this isn't
        // as clean as it could be.
        //
        val genericGsonSubTypeKeys: List<GsonSubTypeKeyAndClass> =
                when (keyType) {
                    SubTypeKeyType.STRING -> gsonSubType.stringValueSubtypes.map {
                        getGsonSubTypeKeyAndClass("\"${it.value}\"", fieldInfo) { it.subtype }
                    }

                    SubTypeKeyType.INTEGER -> gsonSubType.integerValueSubtypes.map {
                        getGsonSubTypeKeyAndClass(it.value.toString(), fieldInfo) { it.subtype }
                    }

                    SubTypeKeyType.BOOLEAN -> gsonSubType.booleanValueSubtypes.map {
                        getGsonSubTypeKeyAndClass(it.value.toString(), fieldInfo) { it.subtype }
                    }
                }

        // Ensure that each subtype inherits from the annotated field.
        val gsonFieldType = SharedFunctions.getRawType(fieldInfo)
        genericGsonSubTypeKeys.forEach {
            validateSubType(gsonFieldType, it.clazzTypeMirror, fieldInfo.element)
        }

        // Inspect the failure outcome values.
        val defaultTypeMirror = SharedFunctions.getMirroredClass(fieldInfo) { gsonSubType.defaultType }

        val defaultsElement = typeHandler.asElement(defaultTypeMirror)
        if (defaultsElement != null) {
            // It is not valid to specify a default type if the failure outcome does not use it.
            if (gsonSubType.subTypeFailureOutcome != GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE) {
                throw ProcessingException("defaultType is only valid if subTypeFailureOutcome is set to NULL_OR_DEFAULT_VALUE", fieldInfo.element)
            }

            // Ensure that the default type inherits from the base type.
            validateSubType(gsonFieldType, defaultTypeMirror, fieldInfo.element)
        }

        val variableName = "${fieldInfo.fieldName}GsonSubtype"
        return SubTypeMetadata(
                className = fieldInfo.fieldName[0].toUpperCase() + fieldInfo.fieldName.substring(1) + "GsonSubtype",
                variableName = variableName,
                getterName = "get${variableName[0].toUpperCase()}${variableName.substring(1)}",
                fieldName = gsonSubType.subTypeKey,
                keyType = keyType,
                gsonSubTypeKeys = genericGsonSubTypeKeys,
                defaultType = defaultsElement?.asType(),
                failureOutcome = gsonSubType.subTypeFailureOutcome)
    }

    private fun validateSubType(baseType: TypeMirror, subType: TypeMirror, fieldElement: Element?) {
        if (!typeHandler.isSubtype(subType, baseType)) {
            throw ProcessingException("subtype $subType does not inherit from $baseType", fieldElement)
        }
    }

    private fun getGsonSubTypeKeyAndClass(key: String,
                                          fieldInfo: FieldInfo,
                                          accessorFunc: () -> Unit): GsonSubTypeKeyAndClass {
        return GsonSubTypeKeyAndClass(key, SharedFunctions.getMirroredClass(fieldInfo, accessorFunc))
    }
}