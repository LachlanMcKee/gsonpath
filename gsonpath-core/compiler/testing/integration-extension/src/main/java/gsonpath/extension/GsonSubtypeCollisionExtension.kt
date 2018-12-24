package gsonpath.extension

import gsonpath.GsonSubtype
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment

class GsonSubtypeCollisionExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'GsonSubtypeCollision' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata

        return fieldInfo.getAnnotation(GsonSubtype::class.java) != null
    }

    override fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): GsonPathExtension.ExtensionResult {

        return GsonPathExtension.ExtensionResult(codeBlock { })
    }

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        return null
    }
}
