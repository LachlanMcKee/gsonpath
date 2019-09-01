package gsonpath.adapter.common

import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.squareup.javapoet.*
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants
import gsonpath.adapter.standard.extension.addException
import gsonpath.util.*

object GsonSubTypeFactory {

    fun createSubTypeMetadata(
            elementTypeName: TypeName,
            subTypeMetadata: SubTypeMetadata): GsonSubTypeResult {

        return GsonSubTypeResult(
                readMethodSpecs = createReadMethod(subTypeMetadata, elementTypeName),
                writeMethodSpecs = createWriteMethod(elementTypeName)
        )
    }

    /**
     * The read method deserializes the entire json object which is inefficient, however it unfortunately the only way
     * to guarantee that the 'type' field is read early enough.
     *
     * Once the object is memory, the type field is located, and then the correct adapter is hopefully found and
     * delegated to. If not, the deserializer may return null, use a default deserializer, or throw an exception
     * depending on the GsonSubtype annotation settings.
     */
    private fun createReadMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName) = AdapterMethodBuilder.createReadMethodBuilder(rawTypeName).applyAndBuild {

        code {
            createVariable(JsonElement::class.java,
                    JSON_ELEMENT,
                    "\$T.parse(${Constants.IN})",
                    Streams::class.java)

            val fieldInfo = subTypeMetadata.gsonSubTypeFieldInfo.apply {
                forEach {
                    newLine()
                    addVariable(it, rawTypeName)
                }
                newLine()
            }

            createVariable(ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName)),
                    DELEGATE_CLASS,
                    "\$T.${subTypeMetadata.classGetterMethodName}(${fieldInfo.joinToString { it.variableName }})",
                    rawTypeName)

            `if`("$DELEGATE_CLASS == ${Constants.NULL}") {
                `return`(Constants.NULL)
            }

            createVariable(rawTypeName, RESULT, "mGson.getAdapter($DELEGATE_CLASS).fromJsonTree($JSON_ELEMENT)")

            `return`(RESULT)
        }
    }

    private fun CodeBlock.Builder.addVariable(fieldInfo: GsonSubTypeFieldInfo, rawTypeName: TypeName) {
        val jsonElementName = fieldInfo.variableName + "_jsonElement"
        createVariable(JsonElement::class.java,
                jsonElementName,
                "$JSON_ELEMENT.getAsJsonObject().get(\"${fieldInfo.jsonKey}\")")

        addStatement("final \$T ${fieldInfo.variableName}", fieldInfo.parameterTypeName)

        ifWithoutClose("$jsonElementName == ${Constants.NULL} || $jsonElementName.isJsonNull()") {
            if (fieldInfo.nullable) {
                assign(fieldInfo.variableName, "null")
            } else {
                addException("cannot deserialize $rawTypeName because the subtype field " +
                        "'${fieldInfo.jsonKey}' is either null or does not exist.")
            }
        }
        `else` {
            val adapterName =
                    if (fieldInfo.parameterTypeName is ParameterizedTypeName)
                        "new com.google.gson.reflect.TypeToken<\$T>(){}" // This is a generic type
                    else
                        "\$T.class"

            assign(fieldInfo.variableName,
                    "${Constants.GET_ADAPTER}($adapterName).fromJsonTree($jsonElementName)",
                    fieldInfo.parameterTypeName)
        }
    }

    /**
     * The write method is substantially simpler, as we do not to consume an entire json object.
     */
    private fun createWriteMethod(
            rawTypeName: TypeName) = AdapterMethodBuilder.createWriteMethodBuilder(rawTypeName).applyAndBuild {

        code {
            newLine()
            `if`("${Constants.VALUE} == ${Constants.NULL}") {
                addStatement("${Constants.OUT}.nullValue()")
                `return`()
            }
            createVariable(TypeAdapter::class.java, DELEGATE_ADAPTER, "mGson.getAdapter(${Constants.VALUE}.getClass())")
        }

        addStatement("$DELEGATE_ADAPTER.write(${Constants.OUT}, ${Constants.VALUE})")
    }

    private const val JSON_ELEMENT = "jsonElement"
    private const val DELEGATE_CLASS = "delegateClass"
    private const val DELEGATE_ADAPTER = "delegateAdapter"
    private const val RESULT = "result"
}