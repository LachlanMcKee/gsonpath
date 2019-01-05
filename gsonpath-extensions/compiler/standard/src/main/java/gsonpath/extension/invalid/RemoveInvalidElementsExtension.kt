package gsonpath.extension.invalid

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.RemoveInvalidElementsUtil
import gsonpath.extension.annotation.RemoveInvalidElements
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import gsonpath.util.assign
import gsonpath.util.codeBlock
import gsonpath.util.createVariable
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

class RemoveInvalidElementsExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'RemoveInvalidElements' Annotation"

    private fun verifyMultipleValuesFieldType(fieldInfo: FieldInfo): FieldType.MultipleValues {
        return when (val fieldType = fieldInfo.fieldType) {
            is FieldType.MultipleValues -> fieldType
            else -> throw ProcessingException("@RemoveInvalidElements can only be used with arrays and collections",
                    fieldInfo.element)
        }
    }

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(RemoveInvalidElements::class.java) == null) {
            return false
        }

        verifyMultipleValuesFieldType(fieldInfo)

        return true
    }

    override fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): GsonPathExtension.ExtensionResult {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val multipleValuesFieldType = verifyMultipleValuesFieldType(fieldInfo)
        val rawTypeName = TypeName.get(multipleValuesFieldType.elementTypeMirror)

        return GsonPathExtension.ExtensionResult(codeBlock {
            if (checkIfResultIsNull) {
                when (multipleValuesFieldType) {
                    is FieldType.MultipleValues.Array -> {
                        createVariable("\$T", variableName, "\$T.removeInvalidElementsArray(\$T.class, mGson, in, \$L)",
                                fieldInfo.fieldType.typeName, CLASS_NAME_UTIL, rawTypeName,
                                createCreateArrayFuncTypeSpec(rawTypeName))
                    }
                    is FieldType.MultipleValues.Collection -> {
                        createVariable("\$T", variableName, "\$T.removeInvalidElementsList(\$T.class, mGson, in)",
                                fieldInfo.fieldType.typeName, CLASS_NAME_UTIL, rawTypeName)
                    }
                }
            } else {
                when (multipleValuesFieldType) {
                    is FieldType.MultipleValues.Array -> {
                        assign(variableName, "\$T.removeInvalidElementsArray(\$T.class, mGson, in, \$L)",
                                CLASS_NAME_UTIL, rawTypeName, createCreateArrayFuncTypeSpec(rawTypeName))
                    }
                    is FieldType.MultipleValues.Collection -> {
                        assign(variableName, "\$T.removeInvalidElementsList(\$T.class, mGson, in)",
                                CLASS_NAME_UTIL, rawTypeName)
                    }
                }
            }
        })
    }

    /**
     * Required to allow converting the list into an array.
     * Type erasure is fun!
     */
    private fun createCreateArrayFuncTypeSpec(arrayType: TypeName): TypeSpec {
        val functionClassName = ClassName.get(RemoveInvalidElementsUtil.CreateArrayFunction::class.java)
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(functionClassName, arrayType))
                .addMethod(MethodSpec.methodBuilder("createArray")
                        .addAnnotation(Override::class.java)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ArrayTypeName.of(arrayType))
                        .addStatement("return new \$T[0]", arrayType)
                        .build())
                .build()
    }

    private companion object {
        private val CLASS_NAME_UTIL = ClassName.get(RemoveInvalidElementsUtil::class.java)
    }
}
