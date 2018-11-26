package gsonpath.extension

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

class RoundFloatUpToIntExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'RoundFloatUpToInt' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(RoundFloatUpToInt::class.java) == null) {
            return false
        }

        if (fieldInfo.typeName != TypeName.INT) {
            throw ProcessingException("Unexpected type found for field annotated with 'RoundFloatUpToInt', only " +
                    "int primitives may be used.", fieldInfo.element)
        }

        return true
    }

    override fun createCodeReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): CodeBlock {

        val (_, variableName) = extensionFieldMetadata

        return codeBlock {
            val floatVariableName = "${variableName}_float"
            createVariable("Float", floatVariableName, "mGson.getAdapter(Float.class).read(in)")

            if (checkIfResultIsNull) {
                addStatement("\$T $variableName", TypeName.INT.box())

                ifWithoutClose("$floatVariableName != null") {
                    assign(variableName, "(int) Math.ceil($floatVariableName)")
                }
                `else` {
                    assign(variableName, "null")
                }
            } else {
                `if`("$floatVariableName != null") {
                    assign(variableName, "(int) Math.ceil($floatVariableName)")
                }
            }
        }
    }

    override fun createCodePostReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        return null
    }
}