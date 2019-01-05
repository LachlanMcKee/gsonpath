package gsonpath.extension.flatten

import com.google.gson.JsonElement
import com.squareup.javapoet.ClassName
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.annotation.FlattenJson
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment

class FlattenJsonExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'FlattenJson' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(FlattenJson::class.java) == null) {
            return false
        }

        if (fieldInfo.fieldType.typeName != CLASS_NAME_STRING) {
            throw ProcessingException("FlattenObject can only be used on String variables", fieldInfo.element)
        }

        return true
    }

    override fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): GsonPathExtension.ExtensionResult {

        val (_, variableName) = extensionFieldMetadata

        return GsonPathExtension.ExtensionResult(codeBlock {
            val jsonElementVariableName = "${variableName}_jsonElement"
            createVariable(CLASS_NAME_JSON_ELEMENT, jsonElementVariableName, "mGson.getAdapter(\$T.class).read(in)",
                    CLASS_NAME_JSON_ELEMENT)

            if (checkIfResultIsNull) {
                addStatement("\$T $variableName", CLASS_NAME_STRING)

                ifWithoutClose("$jsonElementVariableName != null") {
                    assign(variableName, "$jsonElementVariableName.toString()")
                }
                `else` {
                    assign(variableName, "null")
                }
            } else {
                `if`("$jsonElementVariableName != null") {
                    assign(variableName, "$jsonElementVariableName.toString()")
                }
            }
        })
    }

    private companion object {
        private val CLASS_NAME_JSON_ELEMENT: ClassName = ClassName.get(JsonElement::class.java)
    }
}
