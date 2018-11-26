package gsonpath.extension

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.`if`
import gsonpath.util.addEscapedStatement
import gsonpath.util.assign
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment

class DoNothingExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'EmptyStringToNull' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        return true
    }

    override fun createCodeReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): CodeBlock {

        return codeBlock {  }
    }

    override fun createCodePostReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        return null
    }
}
