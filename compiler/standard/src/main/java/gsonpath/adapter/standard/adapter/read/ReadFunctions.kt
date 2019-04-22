package gsonpath.adapter.standard.adapter.read

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import gsonpath.JsonReaderHelper
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterMethodBuilder
import gsonpath.adapter.Constants.BREAK
import gsonpath.adapter.Constants.GET_ADAPTER
import gsonpath.adapter.Constants.IN
import gsonpath.adapter.Constants.NULL
import gsonpath.adapter.standard.extension.ExtensionsHandler
import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonModel
import gsonpath.adapter.standard.model.GsonObject
import gsonpath.adapter.standard.model.MandatoryFieldInfoFactory.MandatoryFieldInfo
import gsonpath.compiler.createDefaultVariableValueForTypeName
import gsonpath.model.FieldType
import gsonpath.util.*

/**
 * public T read(JsonReader in) throws IOException {
 */
class ReadFunctions(private val extensionsHandler: ExtensionsHandler) {

    @Throws(ProcessingException::class)
    fun handleRead(typeSpecBuilder: TypeSpec.Builder, params: ReadParams) {
        typeSpecBuilder.addMethod(AdapterMethodBuilder.createReadMethodBuilder(params.baseElement).applyAndBuild {
            code {
                addInitialisationBlock(params)
                addReadCodeForElements(typeSpecBuilder, params.rootElements, params)
                addMandatoryValuesCheck(params)

                if (!params.requiresConstructorInjection) {
                    // If the class was already defined, return it now.
                    `return`(RESULT)

                } else {
                    // Create the class using the constructor.
                    multiLinedNewObject(params.concreteElement, params.flattenedFields.map { it.variableName })
                }
            }
        })
    }

    private fun CodeBlock.Builder.addInitialisationBlock(params: ReadParams) {
        if (!params.requiresConstructorInjection) {
            createVariableNew("\$T", RESULT, "\$T()", params.concreteElement, params.concreteElement)

        } else {
            params.flattenedFields.forEach {
                createVariable(it.fieldInfo.fieldType.typeName,
                        it.variableName,
                        createDefaultVariableValueForTypeName(it.fieldInfo.fieldType.typeName))
            }
        }

        val readerHelperClassName = ClassName.get(JsonReaderHelper::class.java)
        createVariableNew("\$T", JSON_READER_HELPER, "\$T($IN)",
                readerHelperClassName,
                readerHelperClassName)

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
            typeSpecBuilder: TypeSpec.Builder,
            jsonMapping: GsonObject,
            params: ReadParams,
            recursionCount: Int = 0): Int {

        val jsonMappingSize = jsonMapping.size()
        if (jsonMappingSize == 0) {
            return recursionCount
        }

        return `while`("$JSON_READER_HELPER.handleObject($recursionCount, $jsonMappingSize)") {
            switch("$IN.nextName()") {
                jsonMapping.entries()
                        .fold(recursionCount + 1) { currentOverallRecursionCount, entry ->
                            addReadCodeForModel(
                                    typeSpecBuilder = typeSpecBuilder,
                                    params = params,
                                    key = entry.key,
                                    value = entry.value,
                                    currentOverallRecursionCount = currentOverallRecursionCount)
                        }
                        .also {
                            default {
                                addStatement("$JSON_READER_HELPER.onObjectFieldNotFound($recursionCount)")
                            }
                        }
            }
        }
    }

    private fun CodeBlock.Builder.addReadCodeForModel(
            typeSpecBuilder: TypeSpec.Builder,
            params: ReadParams,
            key: String,
            value: GsonModel,
            currentOverallRecursionCount: Int): Int {

        return case("\"$key\"") {
            when (value) {
                is GsonField -> {
                    writeGsonFieldReader(typeSpecBuilder, value, params.requiresConstructorInjection,
                            params.mandatoryInfoMap[value.fieldInfo.fieldName])

                    // No extra recursion has happened.
                    currentOverallRecursionCount
                }

                is GsonObject -> {
                    addReadCodeForElements(typeSpecBuilder, value, params, currentOverallRecursionCount)
                }

                is GsonArray -> {
                    writeGsonArrayReader(typeSpecBuilder, value, params, key, currentOverallRecursionCount)
                }
            }
        }
    }

    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.writeGsonFieldReader(
            typeSpecBuilder: TypeSpec.Builder,
            gsonField: GsonField,
            requiresConstructorInjection: Boolean,
            mandatoryFieldInfo: MandatoryFieldInfo?) {

        val fieldInfo = gsonField.fieldInfo
        val result = writeGsonFieldReading(typeSpecBuilder, gsonField, requiresConstructorInjection)

        val assignedVariable =
                if (!requiresConstructorInjection) {
                    "$RESULT." + fieldInfo.fieldName
                } else {
                    gsonField.variableName
                }

        if (result.checkIfNull) {
            `if`("${result.variableName} != $NULL") {
                assign(assignedVariable, result.variableName)

                // When a field has been assigned, if it is a mandatory value, we note this down.
                if (mandatoryFieldInfo != null) {
                    assign("$MANDATORY_FIELDS_CHECK_LIST[${mandatoryFieldInfo.indexVariableName}]", "true")
                    newLine()

                    nextControlFlow("else")
                    addEscapedStatement("""throw new gsonpath.JsonFieldNullException("${gsonField.jsonPath}", "${fieldInfo.parentClassName}")""")
                }
            }
        }

        // Execute any extensions and add the code blocks if they exist.
        val extensionsCodeBlock = codeBlock {
            extensionsHandler.executePostRead(gsonField, assignedVariable) { extensionName, validationResult ->
                newLine()
                comment("Extension - $extensionName")
                add(validationResult.codeBlock)
                newLine()
            }
        }

        // Wrap all of the extensions inside a block and potentially wrap it with a null-check.
        if (!extensionsCodeBlock.isEmpty) {
            newLine()
            comment("Gsonpath Extensions")

            // Handle the null-checking for the extensions to avoid repetition inside the extension implementations.
            if (fieldInfo.fieldType !is FieldType.Primitive) {
                `if`("$assignedVariable != $NULL") {
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
            typeSpecBuilder: TypeSpec.Builder,
            gsonField: GsonField,
            requiresConstructorInjection: Boolean): FieldReaderResult {

        val variableName = getVariableName(gsonField, requiresConstructorInjection)
        val checkIfResultIsNull = isCheckIfNullApplicable(gsonField, requiresConstructorInjection)

        if (extensionsHandler.canHandleFieldRead(gsonField, variableName)) {
            extensionsHandler.executeFieldRead(gsonField, variableName, checkIfResultIsNull) { extensionName, readResult ->
                comment("Extension (Read) - $extensionName")
                add(readResult.codeBlock)
                newLine()

                typeSpecBuilder.addFields(readResult.fieldSpecs)
                typeSpecBuilder.addMethods(readResult.methodSpecs)
                typeSpecBuilder.addTypes(readResult.typeSpecs)
            }

        } else {
            val fieldTypeName = gsonField.fieldInfo.fieldType.typeName.box()
            val adapterName =
                    if (fieldTypeName is ParameterizedTypeName)
                        "new com.google.gson.reflect.TypeToken<\$T>(){}" // This is a generic type
                    else
                        "\$T.class"

            if (checkIfResultIsNull) {
                createVariable(fieldTypeName, variableName, "$GET_ADAPTER($adapterName).read($IN)", fieldTypeName)

            } else {
                assign(variableName, "$GET_ADAPTER($adapterName).read($IN)", fieldTypeName)
            }
        }

        return FieldReaderResult(variableName, checkIfResultIsNull)
    }

    private fun CodeBlock.Builder.writeGsonArrayReader(
            typeSpecBuilder: TypeSpec.Builder,
            value: GsonArray,
            params: ReadParams,
            key: String,
            currentOverallRecursionCount: Int): Int {

        val arrayIndexVariableName = "${key}_arrayIndex"
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
            switch(arrayIndexVariableName) {
                writeGsonArrayReaderCases(typeSpecBuilder, value, params, currentOverallRecursionCount)
                        .also {
                            default {
                                addStatement("in.skipValue()")
                            }
                        }
            }.also {
                addStatement("$arrayIndexVariableName++")
            }
        }.also {
            addStatement("in.endArray()")
        }
    }

    private fun CodeBlock.Builder.writeGsonArrayReaderCases(
            typeSpecBuilder: TypeSpec.Builder,
            value: GsonArray,
            params: ReadParams,
            seedValue: Int): Int {

        return value.entries().fold(seedValue) { previousRecursionCount, (arrayIndex, arrayItemValue) ->
            case("$arrayIndex") {
                when (arrayItemValue) {
                    is GsonField -> {
                        writeGsonFieldReader(typeSpecBuilder, arrayItemValue, params.requiresConstructorInjection,
                                params.mandatoryInfoMap[arrayItemValue.fieldInfo.fieldName])

                        // No extra recursion has happened.
                        previousRecursionCount
                    }
                    is GsonObject -> {
                        addReadCodeForElements(typeSpecBuilder, arrayItemValue, params, previousRecursionCount)
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
                addStatement("""throw new gsonpath.JsonFieldNoKeyException($FIELD_NAME, "${params.concreteElement}")""")
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
            val checkIfNull: Boolean)

    private companion object {
        private const val RESULT = "result"
        private const val MANDATORY_FIELDS_CHECK_LIST = "mandatoryFieldsCheckList"
        private const val MANDATORY_FIELDS_SIZE = "MANDATORY_FIELDS_SIZE"
        private const val MANDATORY_FIELD_INDEX = "mandatoryFieldIndex"
        private const val FIELD_NAME = "fieldName"
        private const val JSON_READER_HELPER = "jsonReaderHelper"
    }
}