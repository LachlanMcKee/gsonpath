package gsonpath.util

import com.squareup.javapoet.CodeBlock
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.model.GsonField
import javax.annotation.processing.ProcessingEnvironment

class ExtensionsHandler(private val processingEnvironment: ProcessingEnvironment,
                        private val extensions: List<GsonPathExtension>) {

    fun handle(gsonField: GsonField, variableName: String, handleFunc: (String, CodeBlock) -> Unit) {
        extensions.forEach { extension ->
            val validationCodeBlock: CodeBlock? = extension.createFieldReadCodeBlock(processingEnvironment,
                    ExtensionFieldMetadata(gsonField.fieldInfo, variableName, gsonField.jsonPath, gsonField.isRequired))

            if (validationCodeBlock != null && !validationCodeBlock.isEmpty) {
                handleFunc(extension.extensionName, validationCodeBlock)
            }
        }
    }

}