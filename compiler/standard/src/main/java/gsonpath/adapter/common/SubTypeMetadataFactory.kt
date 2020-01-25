package gsonpath.adapter.common

import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import javax.lang.model.element.TypeElement

interface SubTypeMetadataFactory {
    fun getGsonSubType(gsonSubType: GsonSubtype, element: TypeElement): SubTypeMetadata
}

class SubTypeMetadataFactoryImpl(
        private val gsonSubTypeGetterMapper: GsonSubTypeGetterMapper,
        private val gsonSubTypeFieldInfoMapper: GsonSubTypeFieldInfoMapper,
        private val subTypeJsonKeysValidator: SubTypeJsonKeysValidator,
        private val subTypeGetterValidator: SubTypeGetterValidator) : SubTypeMetadataFactory {

    override fun getGsonSubType(gsonSubType: GsonSubtype, element: TypeElement): SubTypeMetadata {
        subTypeJsonKeysValidator
                .validateJsonKeys(element, gsonSubType.jsonKeys)
                .throwIfNotNull()

        val methodContent = gsonSubTypeGetterMapper.mapElementToGetterMethod(element)

        subTypeGetterValidator
                .validateGsonSubtypeGetterMethod(element, methodContent)
                .throwIfNotNull()

        return SubTypeMetadata(
                gsonSubTypeFieldInfoMapper.mapToFieldInfo(methodContent, gsonSubType.jsonKeys),
                methodContent.methodName
        )
    }

    private fun ProcessingException?.throwIfNotNull() {
        if (this != null) {
            throw this
        }
    }
}