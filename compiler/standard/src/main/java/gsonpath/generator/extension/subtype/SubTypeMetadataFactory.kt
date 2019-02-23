package gsonpath.generator.extension.subtype

import gsonpath.GsonSubTypeFailureOutcome
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.util.TypeHandler
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

interface SubTypeMetadataFactory {
    fun getGsonSubType(
            gsonSubType: GsonSubtype,
            gsonSubTypeCategory: GsonSubTypeCategory,
            fieldName: String,
            element: Element): SubTypeMetadata
}

class SubTypeMetadataFactoryImpl(private val typeHandler: TypeHandler) : SubTypeMetadataFactory {

    override fun getGsonSubType(
            gsonSubType: GsonSubtype,
            gsonSubTypeCategory: GsonSubTypeCategory,
            fieldName: String,
            element: Element): SubTypeMetadata {

        if (gsonSubType.subTypeKey.isBlank()) {
            throw ProcessingException("subTypeKey cannot be blank for GsonSubType", element)
        }

        val keyCount =
                (if (gsonSubType.stringValueSubtypes.isNotEmpty()) 1 else 0) +
                        (if (gsonSubType.integerValueSubtypes.isNotEmpty()) 1 else 0) +
                        (if (gsonSubType.booleanValueSubtypes.isNotEmpty()) 1 else 0)

        if (keyCount > 1) {
            throw ProcessingException("Only one keys array (string, integer or boolean) may be specified for the GsonSubType",
                    element)
        }

        val keyType: SubTypeKeyType =
                when {
                    gsonSubType.stringValueSubtypes.isNotEmpty() -> SubTypeKeyType.STRING
                    gsonSubType.integerValueSubtypes.isNotEmpty() -> SubTypeKeyType.INTEGER
                    gsonSubType.booleanValueSubtypes.isNotEmpty() -> SubTypeKeyType.BOOLEAN
                    else -> throw ProcessingException("Keys must be specified for the GsonSubType", element)
                }

        //
        // Convert the provided keys into a unified type. Unfortunately due to how annotations work, this isn't
        // as clean as it could be.
        //
        val genericGsonSubTypeKeys: List<GsonSubTypeKeyAndClass> =
                when (keyType) {
                    SubTypeKeyType.STRING -> gsonSubType.stringValueSubtypes.map {
                        getGsonSubTypeKeyAndClass("\"${it.value}\"", element) { it.subtype }
                    }

                    SubTypeKeyType.INTEGER -> gsonSubType.integerValueSubtypes.map {
                        getGsonSubTypeKeyAndClass(it.value.toString(), element) { it.subtype }
                    }

                    SubTypeKeyType.BOOLEAN -> gsonSubType.booleanValueSubtypes.map {
                        getGsonSubTypeKeyAndClass(it.value.toString(), element) { it.subtype }
                    }
                }

        validateKeys(element, genericGsonSubTypeKeys, gsonSubTypeCategory)

        // Inspect the failure outcome values.
        val defaultTypeMirror = getMirroredClass(element) { gsonSubType.defaultType }
        validateFailureOutcomes(element, defaultTypeMirror, gsonSubType, gsonSubTypeCategory)

        val variableName = "${fieldName}GsonSubtype"
        return SubTypeMetadata(
                className = fieldName[0].toUpperCase() + fieldName.substring(1) + "GsonSubtype",
                variableName = variableName,
                getterName = "get${variableName[0].toUpperCase()}${variableName.substring(1)}",
                fieldName = gsonSubType.subTypeKey,
                keyType = keyType,
                gsonSubTypeKeys = genericGsonSubTypeKeys,
                defaultType = typeHandler.asElement(defaultTypeMirror)?.asType(),
                failureOutcome = gsonSubType.subTypeFailureOutcome)
    }

    private fun validateKeys(
            element: Element,
            genericGsonSubTypeKeys: List<GsonSubTypeKeyAndClass>,
            gsonSubTypeCategory: GsonSubTypeCategory) {

        // Ensure keys are not duplicated
        genericGsonSubTypeKeys
                .groupingBy { it.key }
                .eachCount()
                .filter { it.value > 1 }
                .keys
                .firstOrNull()
                ?.let {
                    throw ProcessingException("The key '$it' appears more than once", element)
                }

        // Ensure that each subtype inherits from the annotated field.
        genericGsonSubTypeKeys.forEach {
            validateSubType(gsonSubTypeCategory.fieldType.elementTypeMirror, it.classTypeMirror, element)
        }
    }

    private fun validateFailureOutcomes(
            element: Element,
            defaultTypeMirror: TypeMirror,
            gsonSubType: GsonSubtype,
            gsonSubTypeCategory: GsonSubTypeCategory) {

        val defaultsElement = typeHandler.asElement(defaultTypeMirror)
        if (defaultsElement != null) {
            // It is not valid to specify a default type if the failure outcome does not use it.
            if (gsonSubType.subTypeFailureOutcome != GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE) {
                throw ProcessingException("defaultType is only valid if subTypeFailureOutcome is set to NULL_OR_DEFAULT_VALUE", element)
            }

            // Ensure that the default type inherits from the base type.
            validateSubType(gsonSubTypeCategory.fieldType.elementTypeMirror, defaultTypeMirror, element)
        }

        if (gsonSubTypeCategory is GsonSubTypeCategory.SingleValue) {
            if (gsonSubType.subTypeFailureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT) {
                throw ProcessingException("GsonSubTypeFailureOutcome.REMOVE_ELEMENT cannot be used on a type that is not a collection/array", element)
            }
        }
    }

    private fun validateSubType(baseType: TypeMirror, subType: TypeMirror, fieldElement: Element?) {
        if (!typeHandler.isSubtype(subType, baseType)) {
            throw ProcessingException("subtype $subType does not inherit from $baseType", fieldElement)
        }
    }

    private fun getGsonSubTypeKeyAndClass(key: String,
                                          element: Element,
                                          accessorFunc: () -> Unit): GsonSubTypeKeyAndClass {
        val classTypeMirror = getMirroredClass(element, accessorFunc)
        return GsonSubTypeKeyAndClass(key, classTypeMirror, typeHandler.asElement(classTypeMirror)!!)
    }

    private fun getMirroredClass(element: Element, accessorFunc: () -> Unit): TypeMirror {
        return try {
            accessorFunc()
            throw ProcessingException("Unexpected annotation processing defect while obtaining class.", element)
        } catch (mte: MirroredTypeException) {
            mte.typeMirror
        }
    }
}