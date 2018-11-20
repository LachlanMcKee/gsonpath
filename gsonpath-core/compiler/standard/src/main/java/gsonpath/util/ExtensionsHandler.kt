package gsonpath.util

import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.model.GsonField
import javax.annotation.processing.ProcessingEnvironment

class ExtensionsHandler(
        private val processingEnvironment: ProcessingEnvironment,
        private val extensions: List<GsonPathExtension>) {

    fun canHandleFieldRead(gsonField: GsonField, variableName: String): Boolean {
        val supportedExtensions = extensions
                .map { extension ->
                    extension.canHandleFieldRead(processingEnvironment, createMetadata(gsonField, variableName))
                }
                .filter { it }
                .count()

        if (supportedExtensions > 1) {
            throw ProcessingException("It is not possible to guarantee extension ordering, " +
                    "therefore it is illegal to use multiple", gsonField.fieldInfo.element)
        }
        return supportedExtensions == 1
    }

    fun executeFieldRead(gsonField: GsonField, variableName: String, handleFunc: (String, CodeBlock) -> Unit) {
        if (!canHandleFieldRead(gsonField, variableName)) {
            throw IllegalStateException("canHandleFieldRead must be checked before calling this method.")
        }
        extensions.forEach { extension ->
            val extensionFieldMetadata = createMetadata(gsonField, variableName)
            if (extension.canHandleFieldRead(processingEnvironment, extensionFieldMetadata)) {
                handleFunc(extension.extensionName, extension.createCodeReadCodeBlock(
                        processingEnvironment, extensionFieldMetadata))
            }
        }
    }

    fun executePostRead(gsonField: GsonField, variableName: String, handleFunc: (String, CodeBlock) -> Unit) {
        extensions.forEach { extension ->
            extension.createCodePostReadCodeBlock(processingEnvironment, createMetadata(gsonField, variableName))
                    ?.let {
                        handleFunc(extension.extensionName, it)
                    }
        }
    }

    private fun createMetadata(gsonField: GsonField, variableName: String) =
            ExtensionFieldMetadata(gsonField.fieldInfo, variableName, gsonField.jsonPath, gsonField.isRequired)
}