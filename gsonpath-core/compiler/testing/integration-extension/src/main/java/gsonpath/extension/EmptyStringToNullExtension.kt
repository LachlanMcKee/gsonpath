package gsonpath.extension

import com.squareup.javapoet.ClassName
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.`if`
import gsonpath.util.addEscapedStatement
import gsonpath.util.assign
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment

class EmptyStringToNullExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'EmptyStringToNull' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        return false
    }

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        val (fieldInfo, variableName, jsonPath, isRequired) = extensionFieldMetadata

        if (fieldInfo.getAnnotation(EmptyStringToNull::class.java) == null) {
            return null
        }

        if ((fieldInfo.fieldType.typeName != ClassName.get(String::class.java))) {
            throw ProcessingException("Unexpected type found for field annotated with 'EmptyStringToNull', only " +
                    "string classes may be used.", fieldInfo.element)
        }

        return GsonPathExtension.ExtensionResult(codeBlock {
            `if`("$variableName.trim().length() == 0") {
                if (isRequired) {
                    addEscapedStatement("throw new com.google.gson.JsonParseException(" +
                            "\"JSON element '$jsonPath' cannot be blank\")")
                } else {
                    assign(variableName, "null")
                }
            }
        })
    }
}
