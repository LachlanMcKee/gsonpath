package gsonpath.generator.standard.read

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
import gsonpath.generator.standard.SharedFunctions
import gsonpath.model.GsonField
import gsonpath.model.GsonModel
import gsonpath.model.GsonObject
import gsonpath.model.MandatoryFieldInfoFactory.MandatoryFieldInfo
import gsonpath.util.*
import java.io.IOException

class ReadFunctions {

    /**
     * public T read(JsonReader in) throws IOException {
     */
    @Throws(ProcessingException::class)
    fun createReadMethod(params: ReadParams, extensionsHandler: ExtensionsHandler): MethodSpec {
        return MethodSpecExt.overrideMethodBuilder("read").applyAndBuild {
            returns(params.baseElement)
            addParameter(JsonReader::class.java, "in")
            addException(IOException::class.java)
            code {
                comment("Ensure the object is not null.")
                `if`("!isValidValue(in)") {
                    `return`("null")
                }

                addInitialisationBlock(params)
                addReadCodeForElements(params.rootElements, params, extensionsHandler)
                addMandatoryValuesCheck(params)

                if (!params.requiresConstructorInjection) {
                    // If the class was already defined, return it now.
                    `return`("result")

                } else {
                    // Create the class using the constructor.
                    multiLinedNewObject(params.concreteElement, params.flattenedFields.map { it.variableName })
                }
            }
        }
    }

    private fun CodeBlock.Builder.addInitialisationBlock(params: ReadParams) {
        if (!params.requiresConstructorInjection) {
            createVariable("\$T", "result", "\$T()", params.concreteElement, params.concreteElement)

        } else {
            for (gsonField in params.flattenedFields) {
                // Don't initialise primitives, we rely on validation to throw an exception if the value does not exist.
                val typeName = gsonField.fieldInfo.typeName
                val defaultValue = createDefaultVariableValueForTypeName(typeName)

                addStatement("\$T ${gsonField.variableName} = $defaultValue", typeName)
            }
        }

        // If we have any mandatory fields, we need to keep track of what has been assigned.
        if (params.mandatoryInfoMap.isNotEmpty()) {
            createVariable("boolean[]", "mandatoryFieldsCheckList", "boolean[MANDATORY_FIELDS_SIZE]")
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

        addStatement("int $counterVariableName = 0")
        addStatement("in.beginObject()")
        newLine()

        val overallRecursionCount = `while`("in.hasNext()") {

            //
            // Since all the required fields have been mapped, we can avoid calling 'nextName'.
            // This ends up yielding performance improvements on large datasets depending on
            // the ordering of the fields within the JSON.
            //
            `if`("$counterVariableName == $jsonMappingSize") {
                addStatement("in.skipValue()")
                addStatement("continue")
            }
            newLine()

            switch("in.nextName()") {
                val recursionTemp = jsonMapping.entries()
                        .fold(recursionCount + 1) { currentOverallRecursionCount, entry ->
                            addReadCodeForModel(
                                    params = params,
                                    extensionsHandler = extensionsHandler,
                                    key = entry.key,
                                    value = entry.value,
                                    counterVariableName = counterVariableName,
                                    currentOverallRecursionCount = currentOverallRecursionCount)
                        }

                default {
                    addStatement("in.skipValue()")
                }
                return@switch recursionTemp
            }
        }
        newLine()
        addStatement("in.endObject()")

        return overallRecursionCount
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
                    `if`("!isValidValue(in)") {
                        addStatement("break")
                    }
                    addReadCodeForElements(value, params, extensionsHandler, currentOverallRecursionCount)
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

        // Make sure the field's annotations don't have any problems.
        SharedFunctions.validateFieldAnnotations(fieldInfo)

        // Add a new line to improve readability for the multi-lined mapping.
        newLine()

        val result = writeGsonFieldReading(gsonField, requiresConstructorInjection)

        if (result.checkIfNull) {
            `if`("${result.variableName} != null") {

                val assignmentBlock: String = if (!requiresConstructorInjection) {
                    "result." + fieldInfo.fieldName
                } else {
                    gsonField.variableName
                }

                if (result.callToString) {
                    addStatement("$assignmentBlock = ${result.variableName}.toString()")
                } else {
                    addStatement("$assignmentBlock = ${result.variableName}")
                }

                // When a field has been assigned, if it is a mandatory value, we note this down.
                if (mandatoryFieldInfo != null) {
                    addStatement("mandatoryFieldsCheckList[${mandatoryFieldInfo.indexVariableName}] = true")
                    newLine()

                    nextControlFlow("else")
                    addEscapedStatement("""throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '${gsonField.jsonPath}' was null for class '${fieldInfo.parentClassName}'")""")
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
                `if`("${result.variableName} != null") {
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

                addStatement("\$T $variableName = mGson.getAdapter(\$T.class).read(in)",
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
            val variableAssignment = "$variableName = (\$T) ${subTypeMetadata.getterName}().read(in)"

            if (checkIfResultIsNull) {
                addStatement("\$T $variableAssignment", fieldTypeName, fieldTypeName)

            } else {
                addStatement(variableAssignment, fieldTypeName)
            }

        } else {
            // Handle every other possible class by falling back onto the gson adapter.
            val adapterName =
                    if (fieldTypeName is ParameterizedTypeName)
                        "new com.google.gson.reflect.TypeToken<\$T>(){}" // This is a generic type
                    else
                        "\$T.class"

            val variableAssignment = "$variableName = mGson.getAdapter($adapterName).read(in)"

            if (checkIfResultIsNull) {
                addStatement("\$T $variableAssignment", fieldTypeName, fieldTypeName)

            } else {
                addStatement(variableAssignment, fieldTypeName)
            }
        }

        return FieldReaderResult(variableName, checkIfResultIsNull)
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
        `for`("int mandatoryFieldIndex = 0; mandatoryFieldIndex < MANDATORY_FIELDS_SIZE; mandatoryFieldIndex++") {

            newLine()
            comment("Check if a mandatory value is missing.")
            `if`("!mandatoryFieldsCheckList[mandatoryFieldIndex]") {

                // The code must figure out the correct field name to insert into the error message.
                newLine()
                comment("Find the field name of the missing json value.")
                addStatement("String fieldName = null")
                switch("mandatoryFieldIndex") {

                    for ((_, mandatoryFieldInfo) in params.mandatoryInfoMap) {
                        case(mandatoryFieldInfo.indexVariableName) {
                            addEscapedStatement("""fieldName = "${mandatoryFieldInfo.gsonField.jsonPath}"""")
                        }
                    }

                }
                addStatement("""throw new gsonpath.JsonFieldMissingException("Mandatory JSON element '" + fieldName + "' was not found for class '${params.concreteElement}'")""")
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
    }
}