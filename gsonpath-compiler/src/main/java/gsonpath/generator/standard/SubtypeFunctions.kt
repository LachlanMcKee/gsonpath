package gsonpath.generator.standard

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
import gsonpath.compiler.addComment
import gsonpath.generator.standard.SharedFunctions.getMirroredClass
import gsonpath.generator.standard.SharedFunctions.getRawType
import gsonpath.generator.standard.SharedFunctions.isArrayType
import gsonpath.internal.CollectionTypeAdapter
import gsonpath.internal.StrictArrayTypeAdapter
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.model.GsonObjectTreeFactory
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeMirror

val arrayTypeAdapterClassName: ClassName = ClassName.get(StrictArrayTypeAdapter::class.java)

/**
 * Creates the code required for subtype adapters for any fields that use the GsonSubtype annotation.
 */
fun addSubTypeTypeAdapters(processingEnv: ProcessingEnvironment, typeSpecBuilder: TypeSpec.Builder, rootElements: GsonObject) {
    GsonObjectTreeFactory().getFlattenedFieldsFromGsonObject(rootElements)
            .mapNotNull { it.fieldInfo.getAnnotation(GsonSubtype::class.java)?.to(it) }
            .forEach { (subTypeAnnotation, gsonField) ->
                // Ignore any fields that do not have a GsonSubtype annotation.
                val validatedGsonSubType = validateGsonSubType(processingEnv, gsonField, subTypeAnnotation)
                val typeAdapterDetails = getTypeAdapterDetails(processingEnv, gsonField)

                val subTypeAdapterVariableName = getSubTypeAdapterVariableName(gsonField)
                typeSpecBuilder.addField(typeAdapterDetails.typeName, subTypeAdapterVariableName, Modifier.PRIVATE)

                createGetter(processingEnv, typeSpecBuilder, gsonField, validatedGsonSubType)
                createSubTypeAdapter(processingEnv, typeSpecBuilder, gsonField, validatedGsonSubType)
            }
}

/**
 * Obtains the name of the subtype adapter getter method contained within the root level class.
 */
fun getSubTypeGetterName(gsonField: GsonField): String {
    val variableName = getSubTypeAdapterVariableName(gsonField)
    return "get${variableName[0].toUpperCase()}${variableName.substring(1)}"
}

private fun getGsonSubTypeKeyAndClass(key: String,
                                      gsonField: GsonField,
                                      accessorFunc: () -> Unit): GsonSubTypeKeyAndClass {
    return GsonSubTypeKeyAndClass(key, getMirroredClass(gsonField, accessorFunc))
}

/**
 * Validates the GsonSubType annotation and returns a valid version that contains no incorrect data.
 * Any incorrect usages will cause an exception to be thrown.
 */
private fun validateGsonSubType(processingEnv: ProcessingEnvironment, gsonField: GsonField, gsonSubType: GsonSubtype): ValidatedGsonSubType {
    if (gsonSubType.subTypeKey.isBlank()) {
        throw ProcessingException("subTypeKey cannot be blank for GsonSubType", gsonField.fieldInfo.element)
    }

    val keyCount =
            (if (gsonSubType.stringValueSubtypes.isNotEmpty()) 1 else 0) +
                    (if (gsonSubType.integerValueSubtypes.isNotEmpty()) 1 else 0) +
                    (if (gsonSubType.booleanValueSubtypes.isNotEmpty()) 1 else 0)

    if (keyCount > 1) {
        throw ProcessingException("Only one keys array (string, integer or boolean) may be specified for the GsonSubType",
                gsonField.fieldInfo.element)
    }

    val keyType: SubTypeKeyType =
            when {
                gsonSubType.stringValueSubtypes.isNotEmpty() -> SubTypeKeyType.STRING
                gsonSubType.integerValueSubtypes.isNotEmpty() -> SubTypeKeyType.INTEGER
                gsonSubType.booleanValueSubtypes.isNotEmpty() -> SubTypeKeyType.BOOLEAN
                else -> throw ProcessingException("Keys must be specified for the GsonSubType", gsonField.fieldInfo.element)
            }

    //
    // Convert the provided keys into a unified type. Unfortunately due to how annotations work, this isn't
    // as clean as it could be.
    //
    val genericGsonSubTypeKeys: List<GsonSubTypeKeyAndClass> =
            when (keyType) {
                SubTypeKeyType.STRING -> gsonSubType.stringValueSubtypes.map {
                    getGsonSubTypeKeyAndClass("\"${it.value}\"", gsonField) { it.subtype }
                }

                SubTypeKeyType.INTEGER -> gsonSubType.integerValueSubtypes.map {
                    getGsonSubTypeKeyAndClass(it.value.toString(), gsonField) { it.subtype }
                }

                SubTypeKeyType.BOOLEAN -> gsonSubType.booleanValueSubtypes.map {
                    getGsonSubTypeKeyAndClass(it.value.toString(), gsonField) { it.subtype }
                }
            }

    // Ensure that each subtype inherits from the annotated field.
    val gsonFieldType = getRawType(gsonField)
    genericGsonSubTypeKeys.forEach {
        validateSubType(processingEnv, gsonFieldType, it.clazzTypeMirror, gsonField.fieldInfo.element)
    }

    // Inspect the failure outcome values.
    val defaultTypeMirror = getMirroredClass(gsonField) { gsonSubType.defaultType }

    val defaultsElement = processingEnv.typeUtils.asElement(defaultTypeMirror)
    if (defaultsElement != null) {
        // It is not valid to specify a default type if the failure outcome does not use it.
        if (gsonSubType.subTypeFailureOutcome != GsonSubTypeFailureOutcome.NULL_OR_DEFAULT_VALUE) {
            throw ProcessingException("defaultType is only valid if subTypeFailureOutcome is set to NULL_OR_DEFAULT_VALUE", gsonField.fieldInfo.element)
        }

        // Ensure that the default type inherits from the base type.
        validateSubType(processingEnv, gsonFieldType, defaultTypeMirror, gsonField.fieldInfo.element)
    }

    return ValidatedGsonSubType(
            fieldName = gsonSubType.subTypeKey,
            keyType = keyType,
            gsonSubTypeKeys = genericGsonSubTypeKeys,
            defaultType = defaultsElement?.asType(),
            failureOutcome = gsonSubType.subTypeFailureOutcome)
}

private fun validateSubType(processingEnv: ProcessingEnvironment, baseType: TypeMirror, subType: TypeMirror, fieldElement: Element?) {
    if (!processingEnv.typeUtils.isSubtype(subType, baseType)) {
        throw ProcessingException("subtype $subType does not inherit from $baseType", fieldElement)
    }
}

/**
 * Creates the getter for the type adapter.
 * This implementration lazily loads, and then cached the result for subsequent usages.
 */
private fun createGetter(processingEnv: ProcessingEnvironment, typeSpecBuilder: TypeSpec.Builder, gsonField: GsonField,
                         validatedGsonSubType: ValidatedGsonSubType) {

    val variableName = getSubTypeAdapterVariableName(gsonField)
    val typeAdapterDetails = getTypeAdapterDetails(processingEnv, gsonField)

    val getterCodeBuilder = CodeBlock.builder()
            .beginControlFlow("if ($variableName == null)")

    val filterNulls = (validatedGsonSubType.failureOutcome == GsonSubTypeFailureOutcome.REMOVE_ELEMENT)

    if (typeAdapterDetails === TypeAdapterDetails.ArrayTypeAdapter) {
        getterCodeBuilder.addStatement("$variableName = new \$T<>(new ${getSubTypeAdapterClassName(gsonField)}(mGson), \$T.class, $filterNulls)",
                typeAdapterDetails, getRawTypeName(gsonField))
    } else {
        getterCodeBuilder.addStatement("$variableName = new \$T(new ${getSubTypeAdapterClassName(gsonField)}(mGson), $filterNulls)",
                typeAdapterDetails)
    }

    getterCodeBuilder.endControlFlow()
            .addStatement("return $variableName")

    typeSpecBuilder.addMethod(MethodSpec.methodBuilder(getSubTypeGetterName(gsonField))
            .addModifiers(Modifier.PRIVATE)
            .returns(typeAdapterDetails.typeName)

            .addCode(getterCodeBuilder.build())
            .build())
}

/**
 * Creates a collection type adapter class name and uses the fields type as the generic parameter.
 */
private fun getTypeAdapterDetails(processingEnv: ProcessingEnvironment, gsonField: GsonField): TypeAdapterDetails {
    return if (isArrayType(processingEnv, gsonField)) {
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
 * Obtains the name of the subtype adapter field contained within the root level class.
 */
private fun getSubTypeAdapterVariableName(gsonField: GsonField): String {
    return "${gsonField.fieldInfo.fieldName}GsonSubtype"
}

/**
 * Obtains the class name of the subtype adapter contained within the root level class.
 */
private fun getSubTypeAdapterClassName(gsonField: GsonField): String {
    return gsonField.fieldInfo.fieldName[0].toUpperCase() + gsonField.fieldInfo.fieldName.substring(1) + "GsonSubtype"
}

/**
 * Creates the gson 'subtype' type adapter inside of the root level class.
 * <p>
 * Only gson fields that are annotated with 'GsonSubtype' should invoke this method
 */
private fun createSubTypeAdapter(processingEnv: ProcessingEnvironment, typeSpecBuilder: TypeSpec.Builder, gsonField: GsonField,
                                 validatedGsonSubType: ValidatedGsonSubType) {

    val rawTypeName = getRawTypeName(gsonField)

    val subTypeAdapterBuilder = TypeSpec.classBuilder(getSubTypeAdapterClassName(gsonField))
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), rawTypeName))

    // Create the type adapter delegate map.
    val typeAdapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), WildcardTypeName.subtypeOf(rawTypeName))
    val classConstainedType = ParameterizedTypeName.get(ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(rawTypeName))

    val valueMapClassName =
            when (validatedGsonSubType.keyType) {
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

    if (validatedGsonSubType.defaultType != null) {
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
    validatedGsonSubType.gsonSubTypeKeys.forEach {
        val subtypeElement = processingEnv.typeUtils.asElement(it.clazzTypeMirror)

        constructorBuilder.addCode("\n")
        constructorBuilder.addStatement("typeAdaptersDelegatedByValueMap.put(${it.key}, gson.getAdapter(\$T.class))", subtypeElement)
        constructorBuilder.addStatement("typeAdaptersDelegatedByClassMap.put(\$T.class, gson.getAdapter(\$T.class))", subtypeElement, subtypeElement)
    }

    if (validatedGsonSubType.defaultType != null) {
        constructorBuilder.addStatement("defaultTypeAdapterDelegate = gson.getAdapter(\$T.class)", validatedGsonSubType.defaultType)
    }

    subTypeAdapterBuilder.addMethod(constructorBuilder.build())

    // Add the read method.
    val readMethod = MethodSpec.methodBuilder("read")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
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
            .addStatement("\$T typeValueJsonElement = jsonElement.getAsJsonObject().remove(\"${validatedGsonSubType.fieldName}\")", JsonElement::class.java)

            .beginControlFlow("if (typeValueJsonElement == null || typeValueJsonElement.isJsonNull())")
            .addStatement("throw new \$T(\"cannot deserialize $rawTypeName because the subtype field '${validatedGsonSubType.fieldName}' is either null or does not exist.\")",
                    JsonParseException::class.java)

            .endControlFlow()

    // Obtain the value using the correct type.
    when (validatedGsonSubType.keyType) {
        SubTypeKeyType.STRING -> readMethodCodeBuilder.addStatement("java.lang.String value = typeValueJsonElement.getAsString()")
        SubTypeKeyType.INTEGER -> readMethodCodeBuilder.addStatement("int value = typeValueJsonElement.getAsInt()")
        SubTypeKeyType.BOOLEAN -> readMethodCodeBuilder.addStatement("boolean value = typeValueJsonElement.getAsBoolean()")
    }

    readMethodCodeBuilder.addStatement("\$T<? extends \$T> delegate = typeAdaptersDelegatedByValueMap.get(value)", TypeAdapter::class.java, rawTypeName)
            .beginControlFlow("if (delegate == null)")

    if (validatedGsonSubType.defaultType != null) {
        readMethodCodeBuilder.addComment("Use the default type adapter if the type is unknown.")
        readMethodCodeBuilder.addStatement("delegate = defaultTypeAdapterDelegate")
    } else {
        if (validatedGsonSubType.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
            readMethodCodeBuilder.addStatement("throw new \$T(\"Failed to find subtype for value: \" + value)", GsonSubTypeFailureException::class.java)
        } else {
            readMethodCodeBuilder.addStatement("return null")
        }
    }

    readMethodCodeBuilder.endControlFlow()
            .addStatement("\$T result = delegate.fromJsonTree(jsonElement)", rawTypeName)

    if (validatedGsonSubType.failureOutcome == GsonSubTypeFailureOutcome.FAIL) {
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
    val writeMethodBuilder = MethodSpec.methodBuilder("write")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(JsonWriter::class.java, "out")
            .addParameter(rawTypeName, "value")
            .addException(IOException::class.java)

            .beginControlFlow("if (value == null)")
            .addStatement("out.nullValue()")
            .addStatement("return")
            .endControlFlow()

            .addStatement("\$T delegate = typeAdaptersDelegatedByClassMap.get(value.getClass())", TypeAdapter::class.java)

    if (validatedGsonSubType.defaultType != null) {
        writeMethodBuilder.beginControlFlow("if (delegate == null)")
        writeMethodBuilder.addStatement("delegate = defaultTypeAdapterDelegate")
        writeMethodBuilder.endControlFlow()
    }

    writeMethodBuilder.addStatement("delegate.write(out, value)", typeAdapterType)
    subTypeAdapterBuilder.addMethod(writeMethodBuilder.build())

    // Add the new subtype type adapter to the root class.
    typeSpecBuilder.addType(subTypeAdapterBuilder.build())
}

/**
 * A data class that is used to convert the annotation 'stringValueSubtypes' 'booleanValueSubtypes' and 'integerValueSubtypes'
 * into a common reusable structure.
 */
data class GsonSubTypeKeyAndClass(val key: String, val clazzTypeMirror: TypeMirror)

data class ValidatedGsonSubType(
        val fieldName: String,
        val keyType: SubTypeKeyType,
        val gsonSubTypeKeys: List<GsonSubTypeKeyAndClass>,
        val defaultType: TypeMirror?,
        val failureOutcome: GsonSubTypeFailureOutcome)

/**
 * The type of value used when determining the correct subtype
 */
enum class SubTypeKeyType {
    STRING, INTEGER, BOOLEAN
}

sealed class TypeAdapterDetails(val typeName: TypeName) {
    object ArrayTypeAdapter : TypeAdapterDetails(arrayTypeAdapterClassName)
    class CollectionTypeAdapter(typeName: TypeName) : TypeAdapterDetails(typeName)
}