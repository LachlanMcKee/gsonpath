package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import com.google.gson.annotations.SerializedName
import gsonpath.ProcessingException
import gsonpath.annotation.EnumGsonAdapter
import gsonpath.util.AnnotationFetcher
import gsonpath.util.FieldElementContent
import gsonpath.util.TypeHandler
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class EnumAdapterPropertiesFactory(
        private val typeHandler: TypeHandler,
        private val annotationFetcher: AnnotationFetcher,
        private val enumFieldLabelMapper: EnumFieldLabelMapper
) {
    fun create(
            enumElement: TypeElement,
            fieldNamingPolicy: FieldNamingPolicy): EnumAdapterProperties {

        val enumFields = typeHandler.getFields(enumElement) { it.kind == ElementKind.ENUM_CONSTANT }

        return EnumAdapterProperties(
                enumTypeName = typeHandler.getClassName(enumElement),
                fields = enumFields.map { createEnumField(enumElement, it, fieldNamingPolicy) },
                defaultValue = getDefaultValue(enumElement, enumFields, fieldNamingPolicy)
        )
    }

    private fun createEnumField(
            enumElement: TypeElement,
            field: FieldElementContent,
            fieldNamingPolicy: FieldNamingPolicy
    ): EnumAdapterProperties.EnumField {
        val serializedName = annotationFetcher.getAnnotation(enumElement, field.element, SerializedName::class.java)
        val enumConstantName = field.element.simpleName.toString()
        return EnumAdapterProperties.EnumField(
                enumValueTypeName = typeHandler.guessClassName("$enumElement.$enumConstantName"),
                label = serializedName?.value ?: enumFieldLabelMapper.map(enumConstantName, fieldNamingPolicy))
    }

    private fun getDefaultValue(
            enumElement: TypeElement,
            enumFields: List<FieldElementContent>,
            fieldNamingPolicy: FieldNamingPolicy
    ): EnumAdapterProperties.EnumField? {
        return enumFields
                .filter {
                    annotationFetcher
                            .getAnnotation(enumElement, it.element, EnumGsonAdapter.DefaultValue::class.java) != null
                }
                .apply {
                    if (size > 1) throw ProcessingException("Only one DefaultValue can be defined", enumElement)
                }
                .firstOrNull()
                ?.let { createEnumField(enumElement, it, fieldNamingPolicy) }
    }
}