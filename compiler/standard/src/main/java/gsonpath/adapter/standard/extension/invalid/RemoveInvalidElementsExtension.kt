package gsonpath.adapter.standard.extension.invalid

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.adapter.Constants.GSON
import gsonpath.adapter.Constants.GSON_ERRORS
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.RemoveInvalidElementsUtil
import gsonpath.extension.annotation.RemoveInvalidElements
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import gsonpath.util.MethodSpecExt
import gsonpath.util.assign
import gsonpath.util.codeBlock
import gsonpath.util.createVariable
import javax.annotation.processing.ProcessingEnvironment

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
            val typeName = fieldInfo.fieldType.typeName
            when (multipleValuesFieldType) {
                is FieldType.MultipleValues.Array -> {
                    val assignment = "\$T.removeInvalidElementsArray(\$T.class, $GSON, in, $GSON_ERRORS, \$L)"
                    val arrayFuncType = createCreateArrayFuncTypeSpec(rawTypeName)
                    if (checkIfResultIsNull) {
                        createVariable(typeName, variableName, assignment, UTIL_CLASS_NAME, rawTypeName, arrayFuncType)
                    } else {
                        assign(variableName, assignment, UTIL_CLASS_NAME, rawTypeName, arrayFuncType)
                    }
                }
                is FieldType.MultipleValues.Collection -> {
                    val assignment = "\$T.removeInvalidElementsList(\$T.class, $GSON, in, $GSON_ERRORS)"
                    if (checkIfResultIsNull) {
                        createVariable(typeName, variableName, assignment, UTIL_CLASS_NAME, rawTypeName)
                    } else {
                        assign(variableName, assignment, UTIL_CLASS_NAME, rawTypeName)
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
                .addMethod(MethodSpecExt.overrideMethodBuilder("createArray")
                        .returns(ArrayTypeName.of(arrayType))
                        .addStatement("return new \$T[0]", arrayType)
                        .build())
                .build()
    }

    private companion object {
        private val UTIL_CLASS_NAME = ClassName.get(RemoveInvalidElementsUtil::class.java)
    }
}
