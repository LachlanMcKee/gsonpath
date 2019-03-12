package gsonpath.generator.extension.subtype

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.GsonSubTypeFailureException
import gsonpath.GsonSubTypeFailureOutcome
import gsonpath.generator.Constants
import gsonpath.util.*
import java.io.IOException
import javax.lang.model.element.Modifier

object GsonSubTypeFactory {

    fun createSubTypeMetadata(
            elementTypeName: TypeName,
            subTypeMetadata: SubTypeMetadata): GsonSubTypeResult {

        // Create the type adapter delegate map.
        val typeAdapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(elementTypeName))
        val classConstainedType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(elementTypeName))

        val valueMapClassName =
                when (subTypeMetadata.keyType) {
                    SubTypeKeyType.STRING -> ClassName.get(String::class.java)
                    SubTypeKeyType.INTEGER -> TypeName.get(Int::class.java).box()
                    SubTypeKeyType.BOOLEAN -> TypeName.get(Boolean::class.java).box()
                }

        val fieldSpecs = listOfNotNull(
                FieldSpec.builder(TypeNameExt.createMap(valueMapClassName, typeAdapterType), DELEGATE_BY_VALUE_MAP)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL),

                FieldSpec.builder(TypeNameExt.createMap(classConstainedType, typeAdapterType), DELEGATE_BY_CLASS_MAP)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL),

                subTypeMetadata.defaultType?.let {
                    FieldSpec.builder(typeAdapterType, DEFAULT_ADAPTER)
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                }
        )

        return GsonSubTypeResult(
                constructorCodeBlock = createSubTypeConstructorCodeBlock(subTypeMetadata),
                fieldSpecs = fieldSpecs.map(FieldSpec.Builder::build),
                readMethodSpecs = createReadMethod(subTypeMetadata, elementTypeName),
                writeMethodSpecs = createWriteMethod(subTypeMetadata, elementTypeName, typeAdapterType)
        )
    }

    private fun createSubTypeConstructorCodeBlock(subTypeMetadata: SubTypeMetadata) = codeBlock {
        assignNew(DELEGATE_BY_VALUE_MAP, "java.util.HashMap<>()")
        assignNew(DELEGATE_BY_CLASS_MAP, "java.util.HashMap<>()")

        // Instantiate each subtype delegated adapter
        subTypeMetadata.gsonSubTypeKeys.forEach {
            val subtypeElement = it.classElement

            newLine()
            addStatement("$DELEGATE_BY_VALUE_MAP.put(${it.key}, ${Constants.GSON}.getAdapter(\$T.class))", subtypeElement)
            addStatement("$DELEGATE_BY_CLASS_MAP.put(\$T.class, ${Constants.GSON}.getAdapter(\$T.class))",
                    subtypeElement, subtypeElement)
        }

        if (subTypeMetadata.defaultType != null) {
            assign(DEFAULT_ADAPTER, "${Constants.GSON}.getAdapter(\$T.class)", subTypeMetadata.defaultType)
        }
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
            rawTypeName: TypeName) = MethodSpecExt.overrideMethodBuilder("read").applyAndBuild {

        returns(rawTypeName)
        addParameter(JsonReader::class.java, Constants.IN)
        addException(IOException::class.java)
        code {
            val fieldName = subTypeMetadata.fieldName
            createVariable(JsonElement::class.java,
                    JSON_ELEMENT,
                    "\$T.parse(${Constants.IN})",
                    Streams::class.java)

            createVariable(JsonElement::class.java,
                    TYPE_VALUE_JSON_ELEMENT,
                    "$JSON_ELEMENT.getAsJsonObject().get(\"$fieldName\")")

            `if`("$TYPE_VALUE_JSON_ELEMENT == ${Constants.NULL} || $TYPE_VALUE_JSON_ELEMENT.isJsonNull()") {
                addStatement("throw new \$T(\"cannot deserialize $rawTypeName because the subtype field " +
                        "'$fieldName' is either null or does not exist.\")",
                        JsonParseException::class.java)
            }

            // Obtain the value using the correct type.
            when (subTypeMetadata.keyType) {
                SubTypeKeyType.STRING ->
                    createVariable("java.lang.String", Constants.VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsString()")

                SubTypeKeyType.INTEGER ->
                    createVariable("int", Constants.VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsInt()")

                SubTypeKeyType.BOOLEAN ->
                    createVariable("boolean", Constants.VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsBoolean()")
            }

            createVariable(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(rawTypeName)),
                    DELEGATE,
                    "$DELEGATE_BY_VALUE_MAP.get(${Constants.VALUE})")

            `if`("$DELEGATE == ${Constants.NULL}") {
                if (subTypeMetadata.defaultType != null) {
                    comment("Use the default type adapter if the type is unknown.")
                    assign(DELEGATE, DEFAULT_ADAPTER)
                } else {
                    if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                        addStatement("throw new \$T(\"Failed to find subtype for value: \" + ${Constants.VALUE})",
                                GsonSubTypeFailureException::class.java)
                    } else {
                        `return`(Constants.NULL)
                    }
                }
            }
            createVariable(rawTypeName, RESULT, "$DELEGATE.fromJsonTree($JSON_ELEMENT)")

            if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                `if`("$RESULT == ${Constants.NULL}") {
                    addStatement("throw new \$T(\"Failed to deserailize subtype for object: \" + $JSON_ELEMENT)",
                            GsonSubTypeFailureException::class.java)
                }
            }

            `return`(RESULT)
        }
    }

    /**
     * The write method is substantially simpler, as we do not to consume an entire json object.
     */
    private fun createWriteMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName,
            typeAdapterType: TypeName) = MethodSpecExt.overrideMethodBuilder("write").applyAndBuild {

        addParameter(JsonWriter::class.java, Constants.OUT)
        addParameter(rawTypeName, Constants.VALUE)
        addException(IOException::class.java)
        code {
            `if`("${Constants.VALUE} == ${Constants.NULL}") {
                addStatement("${Constants.OUT}.nullValue()")
                `return`()
            }
            createVariable(TypeAdapter::class.java, DELEGATE, "$DELEGATE_BY_CLASS_MAP.get(${Constants.VALUE}.getClass())")
        }

        if (subTypeMetadata.defaultType != null) {
            code {
                `if`("$DELEGATE == ${Constants.NULL}") {
                    assign(DELEGATE, DEFAULT_ADAPTER)
                }
            }
        }

        addStatement("$DELEGATE.write(${Constants.OUT}, ${Constants.VALUE})", typeAdapterType)
    }

    private const val DELEGATE_BY_VALUE_MAP = "typeAdaptersDelegatedByValueMap"
    private const val DELEGATE_BY_CLASS_MAP = "typeAdaptersDelegatedByClassMap"
    private const val DEFAULT_ADAPTER = "defaultTypeAdapterDelegate"
    private const val JSON_ELEMENT = "jsonElement"
    private const val TYPE_VALUE_JSON_ELEMENT = "typeValueJsonElement"
    private const val DELEGATE = "delegate"
    private const val RESULT = "result"
}