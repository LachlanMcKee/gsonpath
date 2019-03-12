package gsonpath.generator.extension.subtype

import com.squareup.javapoet.*
import gsonpath.GsonSubTypeFailureOutcome
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.Constants.NULL
import gsonpath.internal.CollectionTypeAdapter
import gsonpath.internal.StrictArrayTypeAdapter
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

class GsonSubTypeExtension(
        private val typeHandler: TypeHandler,
        private val subTypeMetadataFactory: SubTypeMetadataFactory) : GsonPathExtension {

    override val extensionName: String
        get() = "'GsonSubtype' Annotation"

    private fun determineSubTypeCategory(fieldInfo: FieldInfo): GsonSubTypeCategory {
        return when (val fieldType = fieldInfo.fieldType) {
            is FieldType.MultipleValues -> GsonSubTypeCategory.MultipleValues(fieldType)
            is FieldType.Other -> {
                val fieldElement = typeHandler.asElement(fieldInfo.fieldType.elementTypeMirror)

                if (!fieldElement!!.modifiers.contains(Modifier.FINAL)) {
                    GsonSubTypeCategory.SingleValue(fieldType)
                } else {
                    null
                }
            }
            else -> null
        } ?: throw ProcessingException("@GsonSubtype can only be used with arrays, collections, " +
                "interfaces and non-final classes. Maps and primitives are not supported.",
                fieldInfo.element)
    }

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(GsonSubtype::class.java) == null) {
            return false
        }

        determineSubTypeCategory(fieldInfo)

        return true
    }

    override fun canHandleFieldWrite(processingEnvironment: ProcessingEnvironment, extensionFieldMetadata: ExtensionFieldMetadata): Boolean {
        return canHandleFieldRead(processingEnvironment, extensionFieldMetadata)
    }

    override fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): GsonPathExtension.ExtensionResult {

        val (fieldInfo, variableName) = extensionFieldMetadata
        val fieldTypeName = fieldInfo.fieldType.typeName
        val category = determineSubTypeCategory(fieldInfo)
        val fieldType = category.fieldType
        val elementTypeName = TypeName.get(fieldType.elementTypeMirror)

        val subTypeMetadata = subTypeMetadataFactory.getGsonSubType(
                fieldInfo.getAnnotation(GsonSubtype::class.java)!!,
                category,
                fieldInfo.fieldName,
                fieldInfo.element)

        val subTypeAdapterSpec = GsonSubTypeFactory.createSubTypeAdapter(elementTypeName, subTypeMetadata)

        val typeAdapterDetails = when (category) {
            is GsonSubTypeCategory.MultipleValues -> {
                when (category.fieldType) {
                    is FieldType.MultipleValues.Array -> TypeAdapterDetails.ArrayTypeAdapter
                    is FieldType.MultipleValues.Collection -> {
                        TypeAdapterDetails.CollectionTypeAdapter(ParameterizedTypeName.get(
                                ClassName.get(CollectionTypeAdapter::class.java),
                                TypeName.get(fieldType.elementTypeMirror)))
                    }
                }
            }
            is GsonSubTypeCategory.SingleValue -> {
                TypeAdapterDetails.ValueTypeAdapter(ClassName.bestGuess(subTypeAdapterSpec.name))
            }
        }

        return GsonPathExtension.ExtensionResult(
                fieldSpecs = listOf(FieldSpec.builder(typeAdapterDetails.typeName, subTypeMetadata.variableName, Modifier.PRIVATE).build()),
                methodSpecs = listOf(createGetter(typeAdapterDetails, elementTypeName, subTypeMetadata)),
                typeSpecs = listOf(subTypeAdapterSpec),
                codeBlock = codeBlock {
                    if (checkIfResultIsNull) {
                        createVariable(fieldTypeName, variableName, "(\$T) ${subTypeMetadata.getterName}().read(in)", fieldTypeName)
                    } else {
                        assign(variableName, "(\$T) ${subTypeMetadata.getterName}().read(in)", fieldTypeName)
                    }
                })
    }

    override fun createCodeWriteResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val subTypeMetadata = subTypeMetadataFactory.getGsonSubType(
                fieldInfo.getAnnotation(GsonSubtype::class.java)!!,
                determineSubTypeCategory(fieldInfo),
                fieldInfo.fieldName,
                fieldInfo.element)

        return GsonPathExtension.ExtensionResult(
                codeBlock = codeBlock {
                    addStatement("${subTypeMetadata.getterName}().write(out, $variableName)")
                })
    }

    /**
     * Creates the getter for the type adapter.
     * This implementration lazily loads, and then cached the result for subsequent usages.
     */
    private fun createGetter(
            typeAdapterDetails: TypeAdapterDetails,
            elementTypeName: TypeName,
            subTypeMetadata: SubTypeMetadata): MethodSpec {

        return MethodSpec.methodBuilder(subTypeMetadata.getterName).applyAndBuild {
            addModifiers(Modifier.PRIVATE)
            returns(typeAdapterDetails.typeName)

            code {
                val variableName = subTypeMetadata.variableName
                `if`("$variableName == $NULL") {
                    val filterNulls = (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT)

                    when (typeAdapterDetails) {
                        is TypeAdapterDetails.ArrayTypeAdapter -> {
                            assignNew(variableName,
                                    "\$T<>(new ${subTypeMetadata.className}(mGson), \$T.class, $filterNulls)",
                                    typeAdapterDetails.typeName, elementTypeName)
                        }
                        is TypeAdapterDetails.CollectionTypeAdapter -> {
                            assignNew(variableName,
                                    "\$T(new ${subTypeMetadata.className}(mGson), $filterNulls)",
                                    typeAdapterDetails.typeName)
                        }
                        is TypeAdapterDetails.ValueTypeAdapter -> {
                            assignNew(variableName,
                                    "${subTypeMetadata.className}(mGson)")
                        }
                    }
                }
                `return`(variableName)
            }
        }
    }

    private sealed class TypeAdapterDetails(val typeName: TypeName) {
        object ArrayTypeAdapter : TypeAdapterDetails(arrayTypeAdapterClassName)
        class CollectionTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
        class ValueTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
    }

    private companion object {
        private val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)
    }
}
