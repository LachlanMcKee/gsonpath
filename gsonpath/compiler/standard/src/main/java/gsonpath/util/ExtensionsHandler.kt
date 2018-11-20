package gsonpath.util

import com.squareup.javapoet.CodeBlock
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.model.GsonField
import javax.annotation.processing.ProcessingEnvironment

class ExtensionsHandler(
        private val processingEnvironment: ProcessingEnvironment,
        private val extensions: List<GsonPathExtension>) {

    fun handle(gsonField: GsonField, variableName: String, handleFunc: (String, CodeBlock) -> Unit) {
        extensions.forEach { executeExtension(it, gsonField, variableName, handleFunc) }
    }

    private fun executeExtension(
            extension: GsonPathExtension,
            gsonField: GsonField,
            variableName: String,
            handleFunc: (String, CodeBlock) -> Unit) {

        extension.createFieldReadCodeBlock(processingEnvironment, createMetadata(gsonField, variableName))
                ?.let {
                    if (!it.isEmpty) {
                        handleFunc(extension.extensionName, it)
                    }
                }
    }

    private fun createMetadata(gsonField: GsonField, variableName: String) =
            ExtensionFieldMetadata(gsonField.fieldInfo, variableName, gsonField.jsonPath, gsonField.isRequired)
}