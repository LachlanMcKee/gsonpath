package gsonpath.adapter.standard.extension

import gsonpath.ProcessingException
import gsonpath.adapter.standard.model.GsonField
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import javax.annotation.processing.ProcessingEnvironment

class ExtensionsHandler(
        private val processingEnvironment: ProcessingEnvironment,
        private val extensions: List<GsonPathExtension>) {

    private fun canHandleFieldFunc(
            gsonField: GsonField,
            variableName: String,
            func: (GsonPathExtension, ExtensionFieldMetadata) -> Boolean): Boolean {

        val supportedExtensions = extensions
                .map { func(it, createMetadata(gsonField, variableName)) }
                .filter { it }
                .count()

        if (supportedExtensions > 1) {
            throw ProcessingException("It is not possible to guarantee extension ordering, " +
                    "therefore it is illegal to use multiple", gsonField.fieldInfo.element)
        }
        return supportedExtensions == 1
    }

    fun canHandleFieldRead(gsonField: GsonField, variableName: String): Boolean {
        return canHandleFieldFunc(gsonField, variableName) { extension, metadata ->
            extension.canHandleFieldRead(processingEnvironment, metadata)
        }
    }

    fun canHandleFieldWrite(gsonField: GsonField, variableName: String): Boolean {
        return canHandleFieldFunc(gsonField, variableName) { extension, metadata ->
            extension.canHandleFieldWrite(processingEnvironment, metadata)
        }
    }

    fun executeFieldRead(
            gsonField: GsonField,
            variableName: String,
            checkIfResultIsNull: Boolean,
            handleFunc: (String, GsonPathExtension.ExtensionResult) -> Unit) {

        if (!canHandleFieldRead(gsonField, variableName)) {
            throw IllegalStateException("canHandleFieldRead must be checked before calling this method.")
        }
        extensions.forEach { extension ->
            val extensionFieldMetadata = createMetadata(gsonField, variableName)
            if (extension.canHandleFieldRead(processingEnvironment, extensionFieldMetadata)) {
                handleFunc(extension.extensionName, extension.createCodeReadResult(
                        processingEnvironment, extensionFieldMetadata, checkIfResultIsNull))
            }
        }
    }

    fun executeFieldWrite(gsonField: GsonField, variableName: String, handleFunc: (String, GsonPathExtension.ExtensionResult) -> Unit) {
        if (!canHandleFieldWrite(gsonField, variableName)) {
            throw IllegalStateException("canHandleFieldWrite must be checked before calling this method.")
        }
        extensions.forEach { extension ->
            val extensionFieldMetadata = createMetadata(gsonField, variableName)
            if (extension.canHandleFieldWrite(processingEnvironment, extensionFieldMetadata)) {
                handleFunc(extension.extensionName, extension.createCodeWriteResult(
                        processingEnvironment, extensionFieldMetadata))
            }
        }
    }

    fun executePostRead(gsonField: GsonField, variableName: String, handleFunc: (String, GsonPathExtension.ExtensionResult) -> Unit) {
        extensions.forEach { extension ->
            extension.createCodePostReadResult(processingEnvironment, createMetadata(gsonField, variableName))
                    ?.let {
                        handleFunc(extension.extensionName, it)
                    }
        }
    }

    private fun createMetadata(gsonField: GsonField, variableName: String) =
            ExtensionFieldMetadata(gsonField.fieldInfo, variableName, gsonField.jsonPath, gsonField.isRequired)
}