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

/**
 * A {@link GsonPathExtension} that supports the '@Size' annotation.
 */
class TestExtension : GsonPathExtension {

    override val extensionName: String
        get() = "'EmptyStringToNull' Annotation"

    override fun createFieldReadCodeBlock(processingEnvironment: ProcessingEnvironment,
                                          extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        val (fieldInfo, variableName, jsonPath, isRequired) = extensionFieldMetadata

        if (fieldInfo.getAnnotation(EmptyStringToNull::class.java) == null) {
            return null
        }

        if ((fieldInfo.typeName != ClassName.get(String::class.java))) {
            throw ProcessingException("Unexpected type found for field annotated with 'EmptyStringToNull', only " +
                    "string classes may be used.", fieldInfo.element)
        }

        return codeBlock {
            `if`("$variableName.trim().length() == 0") {
                if (isRequired) {
                    addEscapedStatement("throw new com.google.gson.JsonParseException(" +
                            "\"JSON element '$jsonPath' cannot be blank\")")
                } else {
                    assign(variableName, "null")
                }
            }
        }
    }
}
