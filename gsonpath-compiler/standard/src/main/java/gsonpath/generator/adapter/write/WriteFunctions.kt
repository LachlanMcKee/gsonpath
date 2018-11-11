package gsonpath.generator.adapter.write

import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.generator.Constants.GET_ADAPTER
import gsonpath.generator.Constants.NULL
import gsonpath.generator.Constants.OUT
import gsonpath.generator.Constants.VALUE
import gsonpath.model.GsonArray
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.util.*
import java.io.IOException

/**
 * public void write(JsonWriter out, ImageSizes value) throws IOException {
 */
class WriteFunctions {
    @Throws(ProcessingException::class)
    fun createWriteMethod(params: WriteParams) = MethodSpecExt.overrideMethodBuilder("write").applyAndBuild {
        addParameter(JsonWriter::class.java, OUT)
        addParameter(params.elementClassName, VALUE)
        addException(IOException::class.java)
        code {
            // Initial block which prevents nulls being accessed.
            `if`("$VALUE == $NULL") {
                addStatement("$OUT.nullValue()")
                `return`()
            }
            newLine()
            comment("Begin")
            writeGsonFieldWriter(params.rootElements, params.serializeNulls, true)
        }
    }

    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.writeGsonFieldWriter(
            jsonMapping: GsonObject,
            serializeNulls: Boolean,
            writeKeyName: Boolean,
            currentPath: String = "",
            currentFieldCount: Int = 0): Int {

        addStatement("$OUT.beginObject()")

        return jsonMapping.entries()
                .fold(currentFieldCount) { fieldCount, (key, value) ->
                    when (value) {
                        is GsonObject ->
                            handleObject(value, fieldCount, serializeNulls, currentPath, key, writeKeyName)

                        is GsonField ->
                            handleField(value, fieldCount, serializeNulls, key, writeKeyName)

                        is GsonArray ->
                            handleArray(value, fieldCount, serializeNulls, currentPath, key)
                    }
                }
                .also {
                    comment("End $currentPath")
                    addStatement("$OUT.endObject()")
                }
    }

    private fun CodeBlock.Builder.handleObject(
            value: GsonObject,
            fieldCount: Int,
            serializeNulls: Boolean,
            currentPath: String,
            key: String,
            writeKeyName: Boolean): Int {

        if (value.size() == 0) {
            return fieldCount
        }

        val newPath = currentPath + key

        // Add a comment mentioning what nested object we are current pointing at.
        newLine()
        comment("Begin $newPath")
        addStatement("""$OUT.name("$key")""")

        return writeGsonFieldWriter(value, serializeNulls, writeKeyName, newPath, fieldCount)
    }

    private fun CodeBlock.Builder.handleField(
            value: GsonField,
            fieldCount: Int,
            serializeNulls: Boolean,
            key: String,
            writeKeyName: Boolean): Int {

        val fieldInfo = value.fieldInfo
        val fieldTypeName = fieldInfo.typeName
        val isPrimitive = fieldTypeName.isPrimitive
        val fieldAccessor = fieldInfo.fieldAccessor
        val objectName = "obj$fieldCount"

        createVariable("\$T", objectName, "$VALUE.$fieldAccessor", fieldTypeName)

        if (isPrimitive) {
            if (writeKeyName) {
                addEscapedStatement("""$OUT.name("$key")""")
            }
            writeField(value, objectName, fieldTypeName)
        } else {
            if (serializeNulls) {
                // Since we are serializing nulls, we defer the if-statement until after the name is written.
                if (writeKeyName) {
                    addEscapedStatement("""$OUT.name("$key")""")
                }
                ifWithoutClose("$objectName != $NULL") {
                    writeField(value, objectName, fieldTypeName)
                }
                `else` {
                    addStatement("$OUT.nullValue()")
                }
            } else {
                `if`("$objectName != $NULL") {
                    if (writeKeyName) {
                        addEscapedStatement("""$OUT.name("$key")""")
                    }
                    writeField(value, objectName, fieldTypeName)
                }
            }
        }
        newLine()

        return fieldCount + 1
    }

    private fun CodeBlock.Builder.handleArray(
            gsonArray: GsonArray,
            currentFieldCount: Int,
            serializeNulls: Boolean,
            currentPath: String,
            key: String): Int {

        newLine()
        comment("Begin Array: '$currentPath.$key'")
        addStatement("""out.name("$key")""")
        addStatement("out.beginArray()")
        newLine()

        val maxIndex: Int = gsonArray.entries().asSequence().map { it.key }.max()!!

        val newFieldCount =
                (0..maxIndex).fold(currentFieldCount) { previousFieldCount, arrayIndex ->
                    val arrayElement = gsonArray[arrayIndex]

                    val newPath: String =
                            if (currentPath.isEmpty()) {
                                "$key[$arrayIndex]"
                            } else {
                                "$currentPath.$key[$arrayIndex]"
                            }

                    if (arrayElement == null) {
                        // Add any empty array items if required.
                        add("out.nullValue(); // Set Value: '$newPath'")
                        newLine()

                        return@fold previousFieldCount
                    }

                    if (arrayElement is GsonField) {
                        newLine()
                        comment("Set Value: '$newPath'")

                        return@fold handleField(arrayElement, previousFieldCount, serializeNulls, key, false)

                    } else {
                        newLine()
                        comment("Begin Object: '$newPath'")

                        return@fold writeGsonFieldWriter(arrayElement as GsonObject, serializeNulls, true, newPath, previousFieldCount)
                    }
                }

        comment("End Array: '$key'")
        addStatement("out.endArray()")

        return newFieldCount

    }

    private fun CodeBlock.Builder.writeField(value: GsonField, objectName: String, fieldTypeName: TypeName) {
        val subTypeMetadata = value.subTypeMetadata
        val writeLine = when {
            subTypeMetadata != null -> {
                // If this field uses a subtype annotation, we use the type adapter subclasses instead of gson.
                "${subTypeMetadata.getterName}().write($OUT, $objectName)"
            }
            fieldTypeName is ParameterizedTypeName -> {
                "$GET_ADAPTER(new com.google.gson.reflect.TypeToken<\$T>(){}).write($OUT, $objectName)"
            }
            else -> {
                "$GET_ADAPTER(\$T.class).write($OUT, $objectName)"
            }
        }

        addStatement(writeLine, fieldTypeName.box())
    }
}