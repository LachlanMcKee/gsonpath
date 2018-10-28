package gsonpath.generator.adapter.read

import com.google.gson.JsonElement
import com.google.gson.stream.JsonReader
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.compiler.createDefaultVariableValueForTypeName
import gsonpath.generator.Constants.BREAK
import gsonpath.generator.Constants.CONTINUE
import gsonpath.generator.Constants.GET_ADAPTER
import gsonpath.generator.Constants.IN
import gsonpath.generator.Constants.NULL
import gsonpath.model.GsonArray
import gsonpath.model.GsonField
import gsonpath.model.GsonModel
import gsonpath.model.GsonObject
import gsonpath.model.MandatoryFieldInfoFactory.MandatoryFieldInfo
import gsonpath.util.*
import java.io.IOException

/**
 * public T read(JsonReader in) throws IOException {
 */
class ReadFunctions {

    @Throws(ProcessingException::class)
    fun createReadMethod(params: ReadParams, extensionsHandler: ExtensionsHandler): MethodSpec {
        return MethodSpecExt.overrideMethodBuilder("read").applyAndBuild {
            returns(params.baseElement)
            addParameter(JsonReader::class.java, IN)
            addException(IOException::class.java)
            code {
                comment("Ensure the object is not null.")
                `if`("!isValidValue($IN)") {
                    `return`(NULL)
                }

                addInitialisationBlock(params)
                addReadCodeForElements(params.rootElements, params, extensionsHandler)
                addMandatoryValuesCheck(params)

                if (!params.requiresConstructorInjection) {
                    // If the class was already defined, return it now.
                    `return`(RESULT)

                } else {
                    // Create the class using the constructor.
                    multiLinedNewObject(params.concreteElement, params.flattenedFields.map { it.variableName })
                }
            }
        }
    }

    private fun CodeBlock.Builder.addInitialisationBlock(params: ReadParams) {
        if (!params.requiresConstructorInjection) {
            createVariableNew("\$T", RESULT, "\$T()", params.concreteElement, params.concreteElement)

        } else {
            params.flattenedFields.forEach {
                createVariable("\$T",
                        it.variableName,
                        createDefaultVariableValueForTypeName(it.fieldInfo.typeName),
                        it.fieldInfo.typeName)
            }
        }

        // If we have any mandatory fields, we need to keep track of what has been assigned.
        if (params.mandatoryInfoMap.isNotEmpty()) {
            createVariableNew("boolean[]", MANDATORY_FIELDS_CHECK_LIST, "boolean[$MANDATORY_FIELDS_SIZE]")
        }

        newLine()
    }

    /**
     * Adds the read code for the current level of json mapping.
     * This is a recursive function.
     */
    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.addReadCodeForElements(
            jsonMapping: GsonObject,
            params: ReadParams,
            extensionsHandler: ExtensionsHandler,
            recursionCount: Int = 0): Int {

        val jsonMappingSize = jsonMapping.size()
        if (jsonMappingSize == 0) {
            return recursionCount
        }

        val counterVariableName = "jsonFieldCounter$recursionCount"

        createVariable("int", counterVariableName, "0")
        addStatement("$IN.beginObject()")
        newLine()

        return `while`("$IN.hasNext()") {

            //
            // Since all the required fields have been mapped, we can avoid calling 'nextName'.
            // This ends up yielding performance improvements on large datasets depending on
            // the ordering of the fields within the JSON.
            //
            `if`("$counterVariableName == $jsonMappingSize") {
                addStatement("$IN.skipValue()")
                addStatement(CONTINUE)
            }
            newLine()

            switch("$IN.nextName()") {
                jsonMapping.entries()
                        .fold(recursionCount + 1) { currentOverallRecursionCount, entry ->
                            addReadCodeForModel(
                                    params = params,
                                    extensionsHandler = extensionsHandler,
                                    key = entry.key,
                                    value = entry.value,
                                    counterVariableName = counterVariableName,
                                    currentOverallRecursionCount = currentOverallRecursionCount)
                        }
                        .also {
                            default {
                                addStatement("$IN.skipValue()")
                            }
                        }
            }
        }.also {
            newLine()
            addStatement("$IN.endObject()")
        }
    }

    private fun CodeBlock.Builder.addReadCodeForModel(
            params: ReadParams,
            extensionsHandler: ExtensionsHandler,
            key: String,
            value: GsonModel,
            counterVariableName: String,
            currentOverallRecursionCount: Int): Int {

        return case("\"$key\"") {
            // Increment the counter to ensure we track how many fields we have mapped.
            addStatement("$counterVariableName++")

            when (value) {
                is GsonField -> {
                    writeGsonFieldReader(value, params.requiresConstructorInjection,
                            params.mandatoryInfoMap[value.fieldInfo.fieldName], extensionsHandler)

                    // No extra recursion has happened.
                    currentOverallRecursionCount
                }

                is GsonObject -> {
                    newLine()
                    comment("Ensure the object is not null.")
                    `if`("!isValidValue($IN)") {
                        addStatement(BREAK)
                    }
                    addReadCodeForElements(value, params, extensionsHandler, currentOverallRecursionCount)
                }

                is GsonArray -> {
                    writeGsonArrayReader(value, params, key, currentOverallRecursionCount, extensionsHandler)
                }
            }
        }
    }

    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.writeGsonFieldReader(
            gsonField: GsonField,
            requiresConstructorInjection: Boolean,
            mandatoryFieldInfo: MandatoryFieldInfo?,
            extensionsHandler: ExtensionsHandler) {

        val fieldInfo = gsonField.fieldInfo
        val fieldTypeName = fieldInfo.typeName

        // Add a new line to improve readability for the multi-lined mapping.
        newLine()

        val result = writeGsonFieldReading(gsonField, requiresConstructorInjection)

        if (result.checkIfNull) {
            `if`("${result.variableName} != $NULL") {

                val assignmentBlock: String = if (!requiresConstructorInjection) {
                    "$RESULT." + fieldInfo.fieldName
                } else {
                    gsonField.variableName
                }

                if (result.callToString) {
                    assign(assignmentBlock, "${result.variableName}.toString()")
                } else {
                    assign(assignmentBlock, result.variableName)
                }

                // When a field has been assigned, if it is a mandatory value, we note this down.
                if (mandatoryFieldInfo != null) {
                    assign("$MANDATORY_FIELDS_CHECK_LIST[${mandatoryFieldInfo.indexVariableName}]", "true")
                    newLine()

                    nextControlFlow("else")
                    addEscapedStatement("""throw new $JSON_FIELD_MISSING_EXCEPTION("Mandatory JSON element '${gsonField.jsonPath}' was null for class '${fieldInfo.parentClassName}'")""")
                }

            }
        }

        // Execute any extensions and add the code blocks if they exist.
        val extensionsCodeBlock = codeBlock {
            extensionsHandler.handle(gsonField, result.variableName) { extensionName, validationCodeBlock ->
                newLine()
                comment("Extension - $extensionName")
                add(validationCodeBlock)
                newLine()
            }
        }

        // Wrap all of the extensions inside a block and potentially wrap it with a null-check.
        if (!extensionsCodeBlock.isEmpty) {
            newLine()
            comment("Gsonpath Extensions")

            // Handle the null-checking for the extensions to avoid repetition inside the extension implementations.
            if (!fieldTypeName.isPrimitive) {
                `if`("${result.variableName} != $NULL") {
                    add(extensionsCodeBlock)
                }
            } else {
                add(extensionsCodeBlock)
            }
        }
    }

    /**
     * Writes the Java code for field reading that is not supported by Gson.
     */
    private fun CodeBlock.Builder.writeGsonFieldReading(
            gsonField: GsonField,
            requiresConstructorInjection: Boolean): FieldReaderResult {

        val fieldInfo = gsonField.fieldInfo
        val fieldTypeName = fieldInfo.typeName.box()

        // Special handling for strings.
        if (fieldInfo.typeName == CLASS_NAME_STRING) {
            val annotation = fieldInfo.getAnnotation(FlattenJson::class.java)
            if (annotation != null) {
                val variableName =
                        if (requiresConstructorInjection)
                            "${gsonField.variableName}_safe"
                        else
                            gsonField.variableName

                createVariable("\$T", variableName, "$GET_ADAPTER(\$T.class).read($IN)",
                        CLASS_NAME_JSON_ELEMENT,
                        CLASS_NAME_JSON_ELEMENT)

                // FlattenJson is a special case. We always need to ensure that the JsonObject is not null.
                return FieldReaderResult(variableName, checkIfNull = true, callToString = true)
            }
        }

        val variableName = getVariableName(gsonField, requiresConstructorInjection)
        val checkIfResultIsNull = isCheckIfNullApplicable(gsonField, requiresConstructorInjection)

        val subTypeMetadata = gsonField.subTypeMetadata
        if (subTypeMetadata != null) {
            // If this field uses a subtype annotation, we use the type adapter subclasses instead of gson.
            if (checkIfResultIsNull) {
                createVariable("\$T", variableName, "(\$T) ${subTypeMetadata.getterName}().read($IN)", fieldTypeName, fieldTypeName)

            } else {
                assign(variableName, "(\$T) ${subTypeMetadata.getterName}().read($IN)", fieldTypeName)
            }

        } else {
            // Handle every other possible class by falling back onto the gson adapter.
            val adapterName =
                    if (fieldTypeName is ParameterizedTypeName)
                        "new com.google.gson.reflect.TypeToken<\$T>(){}" // This is a generic type
                    else
                        "\$T.class"

            if (checkIfResultIsNull) {
                createVariable("\$T", variableName, "$GET_ADAPTER($adapterName).read($IN)", fieldTypeName, fieldTypeName)

            } else {
                assign(variableName, "$GET_ADAPTER($adapterName).read($IN)", fieldTypeName)
            }
        }

        return FieldReaderResult(variableName, checkIfResultIsNull)
    }

    private fun CodeBlock.Builder.writeGsonArrayReader(
            value: GsonArray,
            params: ReadParams,
            key: String,
            currentOverallRecursionCount: Int,
            extensionsHandler: ExtensionsHandler): Int {

        val arrayIndexVariableName = key + "_arrayIndex"
        newLine()
        comment("Ensure the array is not null.")
        `if`("!isValidValue(in)") {
            addStatement("break")
        }
        addStatement("in.beginArray()")
        createVariable("int", arrayIndexVariableName, "0")
        newLine()
        comment("Iterate through the array.")

        return `while`("in.hasNext()") {
            switch("\$L", arrayIndexVariableName) {
                writeGsonArrayReaderCases(value, params, currentOverallRecursionCount, extensionsHandler).also {
                    default {
                        addStatement("in.skipValue()")
                    }
                }
            }.also {
                addStatement("\$L++", arrayIndexVariableName)
            }
        }.also {
            addStatement("in.endArray()")
        }
    }

    private fun CodeBlock.Builder.writeGsonArrayReaderCases(
            value: GsonArray,
            params: ReadParams,
            seedValue: Int,
            extensionsHandler: ExtensionsHandler): Int {

        return value.entries().fold(seedValue) { previousRecursionCount, entry ->
            val (arrayIndex, arrayItemValue) = entry
            case("$arrayIndex") {
                when (arrayItemValue) {
                    is GsonField -> {
                        writeGsonFieldReader(arrayItemValue, params.requiresConstructorInjection,
                                params.mandatoryInfoMap[arrayItemValue.fieldInfo.fieldName], extensionsHandler)

                        // No extra recursion has happened.
                        previousRecursionCount
                    }
                    is GsonObject -> {
                        addReadCodeForElements(arrayItemValue, params, extensionsHandler, previousRecursionCount)
                    }
                }
            }
        }
    }

    /**
     * If there are any mandatory fields, we now check if any values have been missed. If there are, an exception will be raised here.
     */
    private fun CodeBlock.Builder.addMandatoryValuesCheck(params: ReadParams) {
        if (params.mandatoryInfoMap.isEmpty()) {
            return
        }

        newLine()
        comment("Mandatory object validation")
        `for`("int $MANDATORY_FIELD_INDEX = 0; $MANDATORY_FIELD_INDEX < $MANDATORY_FIELDS_SIZE; $MANDATORY_FIELD_INDEX++") {

            newLine()
            comment("Check if a mandatory value is missing.")
            `if`("!$MANDATORY_FIELDS_CHECK_LIST[$MANDATORY_FIELD_INDEX]") {

                // The code must figure out the correct field name to insert into the error message.
                newLine()
                comment("Find the field name of the missing json value.")
                createVariable("String", FIELD_NAME, NULL)
                switch(MANDATORY_FIELD_INDEX) {

                    for ((_, mandatoryFieldInfo) in params.mandatoryInfoMap) {
                        case(mandatoryFieldInfo.indexVariableName) {
                            addEscapedStatement("""$FIELD_NAME = "${mandatoryFieldInfo.gsonField.jsonPath}"""")
                        }
                    }

                }
                addStatement("""throw new $JSON_FIELD_MISSING_EXCEPTION("Mandatory JSON element '" + $FIELD_NAME + "' was not found for class '${params.concreteElement}'")""")
            }
        }
    }

    private fun getVariableName(gsonField: GsonField, requiresConstructorInjection: Boolean): String {
        return if (gsonField.isRequired && requiresConstructorInjection)
            "${gsonField.variableName}_safe"
        else
            gsonField.variableName
    }

    private fun isCheckIfNullApplicable(gsonField: GsonField, requiresConstructorInjection: Boolean): Boolean {
        return !requiresConstructorInjection || gsonField.isRequired
    }

    private data class FieldReaderResult(
            val variableName: String,
            val checkIfNull: Boolean,
            val callToString: Boolean = false)

    private companion object {
        private val CLASS_NAME_JSON_ELEMENT: ClassName = ClassName.get(JsonElement::class.java)
        private const val RESULT = "result"
        private const val MANDATORY_FIELDS_CHECK_LIST = "mandatoryFieldsCheckList"
        private const val MANDATORY_FIELDS_SIZE = "MANDATORY_FIELDS_SIZE"
        private const val MANDATORY_FIELD_INDEX = "mandatoryFieldIndex"
        private const val FIELD_NAME = "fieldName"
        private const val JSON_FIELD_MISSING_EXCEPTION = "gsonpath.JsonFieldMissingException"
    }
}