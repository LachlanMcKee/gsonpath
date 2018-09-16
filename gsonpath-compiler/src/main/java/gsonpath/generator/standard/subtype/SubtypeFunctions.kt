package gsonpath.generator.standard.subtype

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
import gsonpath.generator.standard.SharedFunctions.getRawType
import gsonpath.generator.standard.SharedFunctions.isArrayType
import gsonpath.internal.CollectionTypeAdapter
import gsonpath.internal.StrictArrayTypeAdapter
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.model.GsonObjectTreeFactory
import gsonpath.util.*
import java.io.IOException
import javax.lang.model.element.Modifier

class SubtypeFunctions(
        private val typeHandler: TypeHandler,
        private val gsonObjectTreeFactory: GsonObjectTreeFactory) {

    /**
     * Creates the code required for subtype adapters for any fields that use the GsonSubtype annotation.
     */
    fun addSubTypeTypeAdapters(
            typeSpecBuilder: TypeSpec.Builder,
            rootElements: GsonObject) {

        gsonObjectTreeFactory
                .getFlattenedFieldsFromGsonObject(rootElements)
                .mapNotNull { it.subTypeMetadata?.to(it) }
                .forEach { (subTypeMetadata, gsonField) ->
                    val typeAdapterDetails = getTypeAdapterDetails(typeHandler, gsonField)

                    typeSpecBuilder.addField(typeAdapterDetails.typeName, subTypeMetadata.variableName, Modifier.PRIVATE)

                    createGetter(typeHandler, typeSpecBuilder, gsonField, subTypeMetadata)
                    createSubTypeAdapter(typeSpecBuilder, gsonField, subTypeMetadata)
                }
    }

    /**
     * Creates the getter for the type adapter.
     * This implementration lazily loads, and then cached the result for subsequent usages.
     */
    private fun createGetter(
            typeHandler: TypeHandler,
            typeSpecBuilder: TypeSpec.Builder,
            gsonField: GsonField,
            subTypeMetadata: SubTypeMetadata) {

        val variableName = subTypeMetadata.variableName
        val typeAdapterDetails = getTypeAdapterDetails(typeHandler, gsonField)

        typeSpecBuilder.method(subTypeMetadata.getterName) {
            addModifiers(Modifier.PRIVATE)
            returns(typeAdapterDetails.typeName)

            code {
                `if`("$variableName == null") {
                    val filterNulls = (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT)

                    if (typeAdapterDetails === TypeAdapterDetails.ArrayTypeAdapter) {
                        addStatement("$variableName = new \$T<>(new ${subTypeMetadata.className}(mGson), \$T.class, $filterNulls)",
                                typeAdapterDetails.typeName, getRawTypeName(gsonField))
                    } else {
                        addStatement("$variableName = new \$T(new ${subTypeMetadata.className}(mGson), $filterNulls)",
                                typeAdapterDetails.typeName)
                    }
                }
                addStatement("return $variableName")
            }
        }
    }

    /**
     * Creates a collection type adapter class name and uses the fields type as the generic parameter.
     */
    private fun getTypeAdapterDetails(
            typeHandler: TypeHandler,
            gsonField: GsonField): TypeAdapterDetails {

        return if (isArrayType(typeHandler, gsonField)) {
            TypeAdapterDetails.ArrayTypeAdapter
        } else {
            TypeAdapterDetails.CollectionTypeAdapter(ParameterizedTypeName.get(
                    ClassName.get(CollectionTypeAdapter::class.java), TypeName.get(getRawType(gsonField))))
        }
    }

    private fun getRawTypeName(gsonField: GsonField): TypeName {
        return TypeName.get(getRawType(gsonField))
    }

    /**
     * Creates the gson 'subtype' type adapter inside of the root level class.
     * <p>
     * Only gson fields that are annotated with 'GsonSubtype' should invoke this method
     */
    private fun createSubTypeAdapter(
            typeSpecBuilder: TypeSpec.Builder,
            gsonField: GsonField,
            subTypeMetadata: SubTypeMetadata) {

        val rawTypeName = getRawTypeName(gsonField)

        typeSpecBuilder.addType(TypeSpec.classBuilder(subTypeMetadata.className).applyAndBuild {
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

            field("typeAdaptersDelegatedByValueMap", ParameterizedTypeName.get(ClassName.get(Map::class.java), valueMapClassName, typeAdapterType)) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }

            field("typeAdaptersDelegatedByClassMap", ParameterizedTypeName.get(ClassName.get(Map::class.java), classConstainedType, typeAdapterType)) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }

            if (subTypeMetadata.defaultType != null) {
                field("defaultTypeAdapterDelegate", typeAdapterType) {
                    addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                }
            }

            addSubTypeConstructor(subTypeMetadata)
            addReadMethod(subTypeMetadata, rawTypeName)
            addWriteMethod(subTypeMetadata, rawTypeName, typeAdapterType)
        })
    }

    private fun TypeSpec.Builder.addSubTypeConstructor(subTypeMetadata: SubTypeMetadata) = constructor {
        addModifiers(Modifier.PRIVATE)
        addParameter(Gson::class.java, "gson")

        addStatement("typeAdaptersDelegatedByValueMap = new java.util.HashMap<>()")
        addStatement("typeAdaptersDelegatedByClassMap = new java.util.HashMap<>()")

        // Instantiate each subtype delegated adapter
        subTypeMetadata.gsonSubTypeKeys.forEach {
            val subtypeElement = typeHandler.asElement(it.clazzTypeMirror)

            addCode("\n")
            addStatement("typeAdaptersDelegatedByValueMap.put(${it.key}, gson.getAdapter(\$T.class))", subtypeElement)
            addStatement("typeAdaptersDelegatedByClassMap.put(\$T.class, gson.getAdapter(\$T.class))",
                    subtypeElement, subtypeElement)
        }

        if (subTypeMetadata.defaultType != null) {
            addStatement("defaultTypeAdapterDelegate = gson.getAdapter(\$T.class)", subTypeMetadata.defaultType)
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
            rawTypeName: TypeName) = interfaceMethod("read") {

        returns(rawTypeName)
        addParameter(JsonReader::class.java, "in")
        addException(IOException::class.java)
        code {
            addStatement("\$T jsonElement = \$T.parse(in)", JsonElement::class.java, Streams::class.java)
            addStatement("\$T typeValueJsonElement = jsonElement.getAsJsonObject().remove(\"${subTypeMetadata.fieldName}\")",
                    JsonElement::class.java)

            `if`("typeValueJsonElement == null || typeValueJsonElement.isJsonNull()") {
                addStatement("throw new \$T(\"cannot deserialize $rawTypeName because the subtype field '${subTypeMetadata.fieldName}' is either null or does not exist.\")",
                        JsonParseException::class.java)
            }

            // Obtain the value using the correct type.
            addStatement(when (subTypeMetadata.keyType) {
                SubTypeKeyType.STRING -> "java.lang.String value = typeValueJsonElement.getAsString()"
                SubTypeKeyType.INTEGER -> "int value = typeValueJsonElement.getAsInt()"
                SubTypeKeyType.BOOLEAN -> "boolean value = typeValueJsonElement.getAsBoolean()"
            })

            addStatement("\$T<? extends \$T> delegate = typeAdaptersDelegatedByValueMap.get(value)",
                    TypeAdapter::class.java, rawTypeName)

            `if`("delegate == null") {
                if (subTypeMetadata.defaultType != null) {
                    comment("Use the default type adapter if the type is unknown.")
                    addStatement("delegate = defaultTypeAdapterDelegate")
                } else {
                    if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                        addStatement("throw new \$T(\"Failed to find subtype for value: \" + value)",
                                GsonSubTypeFailureException::class.java)
                    } else {
                        addStatement("return null")
                    }
                }
            }
            addStatement("\$T result = delegate.fromJsonTree(jsonElement)", rawTypeName)

            if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                `if`("result == null") {
                    addStatement("throw new \$T(\"Failed to deserailize subtype for object: \" + jsonElement)",
                            GsonSubTypeFailureException::class.java)
                }
            }

            addStatement("return result")
        }
    }

    /**
     * The write method is substantially simpler, as we do not to consume an entire json object.
     */
    private fun TypeSpec.Builder.addWriteMethod(
            subTypeMetadata: SubTypeMetadata,
            rawTypeName: TypeName,
            typeAdapterType: TypeName) = interfaceMethod("write") {

        addParameter(JsonWriter::class.java, "out")
        addParameter(rawTypeName, "value")
        addException(IOException::class.java)
        code {
            `if`("value == null") {
                addStatement("out.nullValue()")
                addStatement("return")
            }
            addStatement("\$T delegate = typeAdaptersDelegatedByClassMap.get(value.getClass())", TypeAdapter::class.java)
        }

        if (subTypeMetadata.defaultType != null) {
            code {
                `if`("delegate == null") {
                    addStatement("delegate = defaultTypeAdapterDelegate")
                }
            }
        }

        addStatement("delegate.write(out, value)", typeAdapterType)
    }

    private sealed class TypeAdapterDetails(val typeName: TypeName) {
        object ArrayTypeAdapter : TypeAdapterDetails(arrayTypeAdapterClassName)
        class CollectionTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
    }

    private companion object {
        private val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)
    }
}