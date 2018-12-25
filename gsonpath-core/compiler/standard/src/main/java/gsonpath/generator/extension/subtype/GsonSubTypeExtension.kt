package gsonpath.generator.extension.subtype

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.*
import gsonpath.GsonSubTypeFailureException
import gsonpath.GsonSubTypeFailureOutcome
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.Constants.GSON
import gsonpath.generator.Constants.IN
import gsonpath.generator.Constants.NULL
import gsonpath.generator.Constants.OUT
import gsonpath.generator.Constants.VALUE
import gsonpath.internal.CollectionTypeAdapter
import gsonpath.internal.StrictArrayTypeAdapter
import gsonpath.model.FieldInfo
import gsonpath.model.FieldType
import gsonpath.util.*
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier

class GsonSubTypeExtension(
        private val typeHandler: TypeHandler,
        private val subTypeMetadataFactory: SubTypeMetadataFactory) : GsonPathExtension {

    override val extensionName: String
        get() = "'GsonSubtype' Annotation"

    private fun verifyMultipleValuesFieldType(fieldInfo: FieldInfo): FieldType.MultipleValues {
        return when (val fieldType = fieldInfo.fieldType) {
            is FieldType.MultipleValues -> fieldType
            else -> throw ProcessingException("@GsonSubtype can only be used with arrays and collections",
                    fieldInfo.element)
        }
    }

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        val (fieldInfo) = extensionFieldMetadata
        if (fieldInfo.getAnnotation(GsonSubtype::class.java) == null) {
            return false
        }

        verifyMultipleValuesFieldType(fieldInfo)

        return true
    }

    override fun canHandleFieldWrite(processingEnvironment: ProcessingEnvironment, extensionFieldMetadata: ExtensionFieldMetadata): Boolean {
        return canHandleFieldRead(processingEnvironment, extensionFieldMetadata)
    }

    override fun createCodeReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): GsonPathExtension.ExtensionResult {

        val (fieldInfo, variableName) = extensionFieldMetadata
        val fieldTypeName = fieldInfo.fieldType.typeName

        val subTypeMetadata = subTypeMetadataFactory.getGsonSubType(
                fieldInfo.getAnnotation(GsonSubtype::class.java)!!, fieldInfo)

        val typeAdapterDetails = when (verifyMultipleValuesFieldType(fieldInfo)) {
            is FieldType.MultipleValues.Array -> TypeAdapterDetails.ArrayTypeAdapter
            is FieldType.MultipleValues.Collection -> {
                TypeAdapterDetails.CollectionTypeAdapter(ParameterizedTypeName.get(
                        ClassName.get(CollectionTypeAdapter::class.java), TypeName.get(typeHandler.getRawType(fieldInfo))))
            }
        }

        return GsonPathExtension.ExtensionResult(
                fieldSpecs = listOf(FieldSpec.builder(typeAdapterDetails.typeName, subTypeMetadata.variableName, Modifier.PRIVATE).build()),
                methodSpecs = listOf(createGetter(typeAdapterDetails, fieldInfo, subTypeMetadata)),
                typeSpecs = listOf(createSubTypeAdapter(fieldInfo, subTypeMetadata)),
                codeBlock = codeBlock {
                    if (checkIfResultIsNull) {
                        createVariable("\$T", variableName, "(\$T) ${subTypeMetadata.getterName}().read(in)", fieldTypeName, fieldTypeName)
                    } else {
                        assign(variableName, "(\$T) ${subTypeMetadata.getterName}().read(in)", fieldTypeName)
                    }
                })
    }

    override fun createCodeWriteResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val subTypeMetadata = subTypeMetadataFactory.getGsonSubType(
                fieldInfo.getAnnotation(GsonSubtype::class.java)!!, fieldInfo)

        return GsonPathExtension.ExtensionResult(
                codeBlock = codeBlock {
                    addStatement("${subTypeMetadata.getterName}().write(out, $variableName)")
                })
    }

    /**
     * Creates the getter for the type adapter.
     * This implementration lazily loads, and then cached the result for subsequent usages.
     */
    private fun createGetter(
            typeAdapterDetails: TypeAdapterDetails,
            fieldInfo: FieldInfo,
            subTypeMetadata: SubTypeMetadata): MethodSpec {

        return MethodSpec.methodBuilder(subTypeMetadata.getterName).applyAndBuild {
            addModifiers(Modifier.PRIVATE)
            returns(typeAdapterDetails.typeName)

            code {
                val variableName = subTypeMetadata.variableName
                `if`("$variableName == $NULL") {
                    val filterNulls = (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT)

                    when (typeAdapterDetails) {
                        is TypeAdapterDetails.ArrayTypeAdapter -> {
                            assignNew(variableName,
                                    "\$T<>(new ${subTypeMetadata.className}(mGson), \$T.class, $filterNulls)",
                                    typeAdapterDetails.typeName, getRawTypeName(fieldInfo))
                        }
                        is TypeAdapterDetails.CollectionTypeAdapter -> {
                            assignNew(variableName,
                                    "\$T(new ${subTypeMetadata.className}(mGson), $filterNulls)",
                                    typeAdapterDetails.typeName)
                        }
                    }
                }
                `return`(variableName)
            }
        }
    }

    private fun getRawTypeName(fieldInfo: FieldInfo): TypeName {
        return TypeName.get(typeHandler.getRawType(fieldInfo))
    }

    /**
     * Creates the gson 'subtype' type adapter inside of the root level class.
     * <p>
     * Only gson fields that are annotated with 'GsonSubtype' should invoke this method
     */
    private fun createSubTypeAdapter(fieldInfo: FieldInfo, subTypeMetadata: SubTypeMetadata): TypeSpec {
        return TypeSpec.classBuilder(subTypeMetadata.className).applyAndBuild {
            val rawTypeName = getRawTypeName(fieldInfo)

            addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), rawTypeName))

            // Create the type adapter delegate map.
            val typeAdapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(rawTypeName))
            val classConstainedType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName))

            val valueMapClassName =
                    when (subTypeMetadata.keyType) {
                        SubTypeKeyType.STRING -> ClassName.get(String::class.java)
                        SubTypeKeyType.INTEGER -> TypeName.get(Int::class.java).box()
                        SubTypeKeyType.BOOLEAN -> TypeName.get(Boolean::class.java).box()
                    }

            field(DELEGATE_BY_VALUE_MAP, TypeNameExt.createMap(valueMapClassName, typeAdapterType)) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }

            field(DELEGATE_BY_CLASS_MAP, TypeNameExt.createMap(classConstainedType, typeAdapterType)) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }

            if (subTypeMetadata.defaultType != null) {
                field(DEFAULT_ADAPTER, typeAdapterType) {
                    addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                }
            }

            addSubTypeConstructor(subTypeMetadata)
            addReadMethod(subTypeMetadata, rawTypeName)
            addWriteMethod(subTypeMetadata, rawTypeName, typeAdapterType)
        }
    }

    private fun TypeSpec.Builder.addSubTypeConstructor(subTypeMetadata: SubTypeMetadata) = constructor {
        addModifiers(Modifier.PRIVATE)
        addParameter(Gson::class.java, GSON)

        code {
            assignNew(DELEGATE_BY_VALUE_MAP, "java.util.HashMap<>()")
            assignNew(DELEGATE_BY_CLASS_MAP, "java.util.HashMap<>()")

            // Instantiate each subtype delegated adapter
            subTypeMetadata.gsonSubTypeKeys.forEach {
                val subtypeElement = it.classElement

                newLine()
                addStatement("$DELEGATE_BY_VALUE_MAP.put(${it.key}, $GSON.getAdapter(\$T.class))", subtypeElement)
                addStatement("$DELEGATE_BY_CLASS_MAP.put(\$T.class, $GSON.getAdapter(\$T.class))",
                        subtypeElement, subtypeElement)
            }

            if (subTypeMetadata.defaultType != null) {
                assign(DEFAULT_ADAPTER, "$GSON.getAdapter(\$T.class)", subTypeMetadata.defaultType)
            }
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
    private fun TypeSpec.Builder.addReadMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName) = overrideMethod("read") {

        returns(rawTypeName)
        addParameter(JsonReader::class.java, IN)
        addException(IOException::class.java)
        code {
            val fieldName = subTypeMetadata.fieldName
            createVariable("\$T",
                    JSON_ELEMENT,
                    "\$T.parse($IN)",
                    JsonElement::class.java,
                    Streams::class.java)

            createVariable("\$T",
                    TYPE_VALUE_JSON_ELEMENT,
                    "$JSON_ELEMENT.getAsJsonObject().get(\"$fieldName\")",
                    JsonElement::class.java)

            `if`("$TYPE_VALUE_JSON_ELEMENT == $NULL || $TYPE_VALUE_JSON_ELEMENT.isJsonNull()") {
                addStatement("throw new \$T(\"cannot deserialize $rawTypeName because the subtype field " +
                        "'$fieldName' is either null or does not exist.\")",
                        JsonParseException::class.java)
            }

            // Obtain the value using the correct type.
            when (subTypeMetadata.keyType) {
                SubTypeKeyType.STRING ->
                    createVariable("java.lang.String", VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsString()")

                SubTypeKeyType.INTEGER ->
                    createVariable("int", VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsInt()")

                SubTypeKeyType.BOOLEAN ->
                    createVariable("boolean", VALUE, "$TYPE_VALUE_JSON_ELEMENT.getAsBoolean()")
            }

            createVariable("\$T<? extends \$T>",
                    DELEGATE,
                    "$DELEGATE_BY_VALUE_MAP.get($VALUE)",
                    TypeAdapter::class.java, rawTypeName)

            `if`("$DELEGATE == $NULL") {
                if (subTypeMetadata.defaultType != null) {
                    comment("Use the default type adapter if the type is unknown.")
                    assign(DELEGATE, DEFAULT_ADAPTER)
                } else {
                    if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                        addStatement("throw new \$T(\"Failed to find subtype for value: \" + $VALUE)",
                                GsonSubTypeFailureException::class.java)
                    } else {
                        `return`(NULL)
                    }
                }
            }
            createVariable("\$T", RESULT, "$DELEGATE.fromJsonTree($JSON_ELEMENT)", rawTypeName)

            if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                `if`("$RESULT == $NULL") {
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
    private fun TypeSpec.Builder.addWriteMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName,
            typeAdapterType: TypeName) = overrideMethod("write") {

        addParameter(JsonWriter::class.java, OUT)
        addParameter(rawTypeName, VALUE)
        addException(IOException::class.java)
        code {
            `if`("$VALUE == $NULL") {
                addStatement("$OUT.nullValue()")
                `return`()
            }
            createVariable("\$T", DELEGATE, "$DELEGATE_BY_CLASS_MAP.get($VALUE.getClass())", TypeAdapter::class.java)
        }

        if (subTypeMetadata.defaultType != null) {
            code {
                `if`("$DELEGATE == $NULL") {
                    assign(DELEGATE, DEFAULT_ADAPTER)
                }
            }
        }

        addStatement("$DELEGATE.write($OUT, $VALUE)", typeAdapterType)
    }

    private sealed class TypeAdapterDetails(val typeName: TypeName) {
        object ArrayTypeAdapter : TypeAdapterDetails(arrayTypeAdapterClassName)
        class CollectionTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
    }

    private companion object {
        private val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)
        private const val DELEGATE_BY_VALUE_MAP = "typeAdaptersDelegatedByValueMap"
        private const val DELEGATE_BY_CLASS_MAP = "typeAdaptersDelegatedByClassMap"
        private const val DEFAULT_ADAPTER = "defaultTypeAdapterDelegate"
        private const val JSON_ELEMENT = "jsonElement"
        private const val TYPE_VALUE_JSON_ELEMENT = "typeValueJsonElement"
        private const val DELEGATE = "delegate"
        private const val RESULT = "result"
    }
}
