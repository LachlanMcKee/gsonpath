package gsonpath.adapter.common

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.WildcardTypeName
import gsonpath.ProcessingException
import gsonpath.adapter.util.NullableUtil
import gsonpath.annotation.GsonSubtype
import gsonpath.annotation.GsonSubtypeGetter
import gsonpath.model.FieldType
import gsonpath.util.MethodElementContent
import gsonpath.util.TypeHandler
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

interface SubTypeMetadataFactory {
    fun getGsonSubType(
            gsonSubType: GsonSubtype,
            fieldType: FieldType.Other,
            fieldName: String,
            element: TypeElement): SubTypeMetadata
}

class SubTypeMetadataFactoryImpl(
        private val typeHandler: TypeHandler) : SubTypeMetadataFactory {

    override fun getGsonSubType(
            gsonSubType: GsonSubtype,
            fieldType: FieldType.Other,
            fieldName: String,
            element: TypeElement): SubTypeMetadata {

        val jsonKeys = gsonSubType.jsonKeys
        validateJsonKeys(element, jsonKeys)

        return getGsonSubtypeGetterMethod(element)
                .also { methodContent -> validateGsonSubtypeGetterMethod(element, methodContent) }
                .let { methodContent -> createSubTypeMetadata(methodContent, jsonKeys) }
    }

    private fun validateJsonKeys(classElement: TypeElement, jsonKeys: Array<String>) {
        if (jsonKeys.isEmpty()) {
            throw ProcessingException("At least one json key must be defined for GsonSubType", classElement)
        }

        if (jsonKeys.any { it.isBlank() }) {
            throw ProcessingException("A blank json key is not valid for GsonSubType", classElement)
        }

        jsonKeys.groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .keys
                .firstOrNull()
                .let {
                    if (it != null) {
                        throw ProcessingException("The json key '\"$it\"' appears more than once", classElement)
                    }
                }
    }

    private fun getGsonSubtypeGetterMethod(classElement: TypeElement): MethodElementContent {
        return typeHandler.getMethods(classElement)
                .filter { it.element.getAnnotation(GsonSubtypeGetter::class.java) != null }
                .let {
                    when {
                        it.isEmpty() -> throw ProcessingException("An @GsonSubtypeGetter method must be defined. See the annotation for more information", classElement)
                        it.size > 1 -> throw ProcessingException("Only one @GsonSubtypeGetter method may exist", classElement)
                        else -> it.first()
                    }
                }
    }

    private fun validateGsonSubtypeGetterMethod(classElement: TypeElement, methodContent: MethodElementContent) {
        val actualReturnType = typeHandler.getTypeName(methodContent.generifiedElement.returnType)
        val elementTypeName = TypeName.get(classElement.asType())
        val expectedReturnType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(elementTypeName))

        if (actualReturnType != expectedReturnType) {
            throw ProcessingException("Incorrect return type for @GsonSubtypeGetter method. It must be Class<? extends ${classElement.simpleName}>", methodContent.element)
        }
    }

    private fun createSubTypeMetadata(methodContent: MethodElementContent, jsonKeys: Array<String>): SubTypeMetadata {
        val executableType = methodContent.element as ExecutableElement

        val parameters = executableType.parameters
        if (parameters.size != jsonKeys.size) {
            throw ProcessingException("The parameters size does not match the json keys size", methodContent.element)
        }

        val fieldInfoList = parameters.zip(jsonKeys)
                .mapIndexed { index, (parameter, key) ->
                    val nonNullAnnotationExists = parameter.annotationMirrors
                            .any { NullableUtil.isNullableKeyword(it.annotationType.asElement().simpleName.toString()) }

                    val parameterTypeName = TypeName.get(parameter.asType())
                    val nullable = !nonNullAnnotationExists && !parameterTypeName.isPrimitive

                    GsonSubTypeFieldInfo(key, "subTypeElement$index", parameterTypeName, nullable)
                }

        return SubTypeMetadata(
                fieldInfoList,
                methodContent.element.simpleName.toString()
        )
    }
}