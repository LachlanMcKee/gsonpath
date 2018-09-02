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
import gsonpath.util.MethodSpecExt
import gsonpath.util.TypeHandler
import gsonpath.util.addComment
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
                    createSubTypeAdapter(typeHandler, typeSpecBuilder, gsonField, subTypeMetadata)
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

        val getterCodeBuilder = CodeBlock.builder()
                .beginControlFlow("if ($variableName == null)")

        val filterNulls = (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT)

        if (typeAdapterDetails === TypeAdapterDetails.ArrayTypeAdapter) {
            getterCodeBuilder.addStatement("$variableName = new \$T<>(new ${subTypeMetadata.className}(mGson), \$T.class, $filterNulls)",
                    typeAdapterDetails.typeName, getRawTypeName(gsonField))
        } else {
            getterCodeBuilder.addStatement("$variableName = new \$T(new ${subTypeMetadata.className}(mGson), $filterNulls)",
                    typeAdapterDetails.typeName)
        }

        getterCodeBuilder.endControlFlow()
                .addStatement("return $variableName")

        typeSpecBuilder.addMethod(MethodSpec.methodBuilder(subTypeMetadata.getterName)
                .addModifiers(Modifier.PRIVATE)
                .returns(typeAdapterDetails.typeName)

                .addCode(getterCodeBuilder.build())
                .build())
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
            typeHandler: TypeHandler,
            typeSpecBuilder: TypeSpec.Builder,
            gsonField: GsonField,
            subTypeMetadata: SubTypeMetadata) {

        val rawTypeName = getRawTypeName(gsonField)

        val subTypeAdapterBuilder = TypeSpec.classBuilder(subTypeMetadata.className)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), rawTypeName))

        // Create the type adapter delegate map.
        val typeAdapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(rawTypeName))
        val classConstainedType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName))

        val valueMapClassName =
                when (subTypeMetadata.keyType) {
                    SubTypeKeyType.STRING -> ClassName.get(String::class.java)
                    SubTypeKeyType.INTEGER -> TypeName.get(Int::class.java).box()
                    SubTypeKeyType.BOOLEAN -> TypeName.get(Boolean::class.java).box()
                }

        subTypeAdapterBuilder.addField(
                FieldSpec.builder(
                        ParameterizedTypeName.get(ClassName.get(Map::class.java), valueMapClassName, typeAdapterType), "typeAdaptersDelegatedByValueMap")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .build())

        subTypeAdapterBuilder.addField(
                FieldSpec.builder(
                        ParameterizedTypeName.get(ClassName.get(Map::class.java), classConstainedType, typeAdapterType), "typeAdaptersDelegatedByClassMap")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .build())

        if (subTypeMetadata.defaultType != null) {
            subTypeAdapterBuilder.addField(
                    FieldSpec.builder(
                            typeAdapterType, "defaultTypeAdapterDelegate")
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                            .build())
        }

        // Add the constructor
        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Gson::class.java, "gson")

                .addStatement("typeAdaptersDelegatedByValueMap = new java.util.HashMap<>()")
                .addStatement("typeAdaptersDelegatedByClassMap = new java.util.HashMap<>()")

        // Instantiate each subtype delegated adapter
        subTypeMetadata.gsonSubTypeKeys.forEach {
            val subtypeElement = typeHandler.asElement(it.clazzTypeMirror)

            constructorBuilder.addCode("\n")
            constructorBuilder.addStatement("typeAdaptersDelegatedByValueMap.put(${it.key}, gson.getAdapter(\$T.class))", subtypeElement)
            constructorBuilder.addStatement("typeAdaptersDelegatedByClassMap.put(\$T.class, gson.getAdapter(\$T.class))", subtypeElement, subtypeElement)
        }

        if (subTypeMetadata.defaultType != null) {
            constructorBuilder.addStatement("defaultTypeAdapterDelegate = gson.getAdapter(\$T.class)", subTypeMetadata.defaultType)
        }

        subTypeAdapterBuilder.addMethod(constructorBuilder.build())

        // Add the read method.
        val readMethod = MethodSpecExt.interfaceMethodBuilder("read")
                .returns(rawTypeName)
                .addParameter(JsonReader::class.java, "in")
                .addException(IOException::class.java)

        val readMethodCodeBuilder = CodeBlock.builder()
        //
        // The read method deserializes the entire json object which is inefficient, however it unfortunately the only way
        // to guarantee that the 'type' field is read early enough.
        //
        // Once the object is memory, the type field is located, and then the correct adapter is hopefully found and delegated
        // to. If not, the deserializer may return null, use a default deserializer, or throw an exception depending on the
        // GsonSubtype annotation settings.
        //
        readMethodCodeBuilder.addStatement("\$T jsonElement = \$T.parse(in)", JsonElement::class.java, Streams::class.java)
                .addStatement("\$T typeValueJsonElement = jsonElement.getAsJsonObject().remove(\"${subTypeMetadata.fieldName}\")", JsonElement::class.java)

                .beginControlFlow("if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull())")
                .addStatement("throw new \$T(\"cannot deserialize $rawTypeName because the subtype field '${subTypeMetadata.fieldName}' is either null or does not exist.\")",
                        JsonParseException::class.java)

                .endControlFlow()

        // Obtain the value using the correct type.
        when (subTypeMetadata.keyType) {
            SubTypeKeyType.STRING -> readMethodCodeBuilder.addStatement("java.lang.String value = typeValueJsonElement.getAsString()")
            SubTypeKeyType.INTEGER -> readMethodCodeBuilder.addStatement("int value = typeValueJsonElement.getAsInt()")
            SubTypeKeyType.BOOLEAN -> readMethodCodeBuilder.addStatement("boolean value = typeValueJsonElement.getAsBoolean()")
        }

        readMethodCodeBuilder.addStatement("\$T<? extends \$T> delegate = typeAdaptersDelegatedByValueMap.get(value)", TypeAdapter::class.java, rawTypeName)
                .beginControlFlow("if (delegate == null)")

        if (subTypeMetadata.defaultType != null) {
            readMethodCodeBuilder.addComment("Use the default type adapter if the type is unknown.")
            readMethodCodeBuilder.addStatement("delegate = defaultTypeAdapterDelegate")
        } else {
            if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
                readMethodCodeBuilder.addStatement("throw new \$T(\"Failed to find subtype for value: \" + value)", GsonSubTypeFailureException::class.java)
            } else {
                readMethodCodeBuilder.addStatement("return null")
            }
        }

        readMethodCodeBuilder.endControlFlow()
                .addStatement("\$T result = delegate.fromJsonTree(jsonElement)", rawTypeName)

        if (subTypeMetadata.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
            readMethodCodeBuilder.beginControlFlow("if (result == null)")
                    .addStatement("throw new \$T(\"Failed to deserailize subtype for object: \" + jsonElement)", GsonSubTypeFailureException::class.java)
                    .endControlFlow()
        }

        readMethodCodeBuilder.addStatement("return result")
        readMethod.addCode(readMethodCodeBuilder.build())
        subTypeAdapterBuilder.addMethod(readMethod.build())

        //
        // Add the write method
        // The write method is substantially simpler, as we do not to consume an entire json object.
        //
        val writeMethodBuilder = MethodSpecExt.interfaceMethodBuilder("write")
                .addParameter(JsonWriter::class.java, "out")
                .addParameter(rawTypeName, "value")
                .addException(IOException::class.java)

                .beginControlFlow("if (value == null)")
                .addStatement("out.nullValue()")
                .addStatement("return")
                .endControlFlow()

                .addStatement("\$T delegate = typeAdaptersDelegatedByClassMap.get(value.getClass())", TypeAdapter::class.java)

        if (subTypeMetadata.defaultType != null) {
            writeMethodBuilder.beginControlFlow("if (delegate == null)")
            writeMethodBuilder.addStatement("delegate = defaultTypeAdapterDelegate")
            writeMethodBuilder.endControlFlow()
        }

        writeMethodBuilder.addStatement("delegate.write(out, value)", typeAdapterType)
        subTypeAdapterBuilder.addMethod(writeMethodBuilder.build())

        // Add the new subtype type adapter to the root class.
        typeSpecBuilder.addType(subTypeAdapterBuilder.build())
    }

    private sealed class TypeAdapterDetails(val typeName: TypeName) {
        object ArrayTypeAdapter : TypeAdapterDetails(arrayTypeAdapterClassName)
        class CollectionTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
    }

    private companion object {
        private val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)
    }
}