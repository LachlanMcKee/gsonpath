package gsonpath.extension.invalid

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.RemoveInvalidElementsUtil
import gsonpath.extension.annotation.RemoveInvalidElements
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import gsonpath.util.ProcessorTypeHandler
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

        val rawTypeName = TypeName.get(ProcessorTypeHandler(processingEnvironment).getRawType(fieldInfo))

        val methodName = when (verifyMultipleValuesFieldType(fieldInfo)) {
            is FieldType.MultipleValues.Array -> "removeInvalidElementsArray"
            is FieldType.MultipleValues.Collection -> "removeInvalidElementsList"
        }
        val assignment = "\$T.$methodName(\$T.class, mGson, in)"

        return GsonPathExtension.ExtensionResult(codeBlock {
            if (checkIfResultIsNull) {
                createVariable("\$T", variableName, assignment, fieldInfo.fieldType.typeName, CLASS_NAME_UTIL, rawTypeName)
            } else {
                assign(variableName, assignment, CLASS_NAME_UTIL, rawTypeName)
            }
        })
    }

    private companion object {
        private val CLASS_NAME_UTIL = ClassName.get(RemoveInvalidElementsUtil::class.java)
    }
}
