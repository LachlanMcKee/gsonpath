package gsonpath.extension.invalid

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.RemoveInvalidElementsUtil
import gsonpath.extension.annotation.RemoveInvalidElements
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

class RemoveInvalidElementsExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'RemoveInvalidElements' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(RemoveInvalidElements::class.java) == null) {
            return false
        }

        ProcessorTypeHandler(processingEnvironment).getMultipleValuesFieldType(fieldInfo)

        return true
    }

    override fun createCodeReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): CodeBlock {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val rawTypeName = TypeName.get(ProcessorTypeHandler(processingEnvironment).getRawType(fieldInfo))

        val methodName = when (ProcessorTypeHandler(processingEnvironment).getMultipleValuesFieldType(fieldInfo)) {
            MultipleValuesFieldType.COLLECTION -> "removeInvalidElementsList"
            MultipleValuesFieldType.ARRAY -> "removeInvalidElementsArray"
        }
        val assignment = "\$T.$methodName(\$T.class, mGson, in)"

        return codeBlock {
            if (checkIfResultIsNull) {
                createVariable("\$T", variableName, assignment, fieldInfo.typeName, CLASS_NAME_UTIL, rawTypeName)
            } else {
                assign(variableName, assignment, CLASS_NAME_UTIL, rawTypeName)
            }
        }
    }

    private companion object {
        private val CLASS_NAME_UTIL = ClassName.get(RemoveInvalidElementsUtil::class.java)
    }
}
