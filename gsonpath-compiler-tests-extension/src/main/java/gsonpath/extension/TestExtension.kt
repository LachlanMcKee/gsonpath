package gsonpath.extension

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.compiler.addEscapedStatement

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

        return CodeBlock.builder()
                .beginControlFlow("if ($variableName.trim().length() == 0)")
                .apply {
                    if (isRequired) {
                        addEscapedStatement("throw new com.google.gson.JsonParseException(" +
                                "\"JSON element '$jsonPath' cannot be blank\")")
                    } else {
                        addStatement("$variableName = null")
                    }
                }
                .endControlFlow()
                .build()
    }
}
