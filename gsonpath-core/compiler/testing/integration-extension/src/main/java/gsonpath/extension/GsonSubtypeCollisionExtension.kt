package gsonpath.extension

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.`if`
import gsonpath.util.addEscapedStatement
import gsonpath.util.assign
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
            checkIfResultIsNull: Boolean): CodeBlock {

        return codeBlock {  }
    }

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        return null
    }
}
