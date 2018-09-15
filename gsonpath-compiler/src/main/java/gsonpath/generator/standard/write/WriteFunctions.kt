package gsonpath.generator.standard.write

import com.google.gson.stream.JsonWriter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.ProcessingException
import gsonpath.generator.standard.SharedFunctions
import gsonpath.model.GsonField
import gsonpath.model.GsonObject
import gsonpath.util.*
import java.io.IOException

class WriteFunctions {
    /**
     * public void write(JsonWriter out, ImageSizes value) throws IOException {
     */
    @Throws(ProcessingException::class)
    fun createWriteMethod(
            elementClassName: ClassName,
            rootElements: GsonObject,
            serializeNulls: Boolean): MethodSpec {

        return MethodSpecExt.interfaceMethodBuilder("write").applyAndBuild {
            addParameter(JsonWriter::class.java, "out")
            addParameter(elementClassName, "value")
            addException(IOException::class.java)
            code {
                // Initial block which prevents nulls being accessed.
                ifBlock("value == null") {
                    addStatement("out.nullValue()")
                    addStatement("return")
                }

                addNewLine()
                addComment("Begin")
                writeGsonFieldWriter(rootElements, "", serializeNulls, 0)
            }
        }
    }

    @Throws(ProcessingException::class)
    private fun CodeBlock.Builder.writeGsonFieldWriter(
            jsonMapping: GsonObject,
            currentPath: String,
            serializeNulls: Boolean,
            currentFieldCount: Int): Int {

        addStatement("out.beginObject()")

        val overallFieldCount: Int = jsonMapping.entries()
                .fold(currentFieldCount) { fieldCount, (key, value) ->
                    when (value) {
                        is GsonObject -> handleObject(value, currentPath, key, serializeNulls, fieldCount)
                        is GsonField -> handleField(value, fieldCount, serializeNulls, key)
                    }
                }

        addComment("End $currentPath")
        addStatement("out.endObject()")

        return overallFieldCount
    }

    private fun CodeBlock.Builder.handleObject(
            value: GsonObject,
            currentPath: String,
            key: String,
            serializeNulls: Boolean,
            fieldCount: Int): Int {

        return if (value.size() > 0) {
            val newPath: String = if (currentPath.isNotEmpty()) {
                currentPath + key
            } else {
                key
            }

            // Add a comment mentioning what nested object we are current pointing at.
            addNewLine()
            addComment("Begin $newPath")
            addStatement("""out.name("$key")""")

            writeGsonFieldWriter(value, newPath, serializeNulls, fieldCount)
        } else {
            fieldCount
        }
    }

    private fun CodeBlock.Builder.handleField(
            value: GsonField,
            fieldCount: Int,
            serializeNulls: Boolean,
            key: String): Int {

        val fieldInfo = value.fieldInfo

        // Make sure the field's annotations don't have any problems.
        SharedFunctions.validateFieldAnnotations(fieldInfo)

        val fieldTypeName = fieldInfo.typeName
        val isPrimitive = fieldTypeName.isPrimitive

        val objectName = "obj$fieldCount"

        addStatement("\$T $objectName = value.${fieldInfo.fieldAccessor}", fieldTypeName)

        // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
        if (!isPrimitive && !serializeNulls) {
            beginControlFlow("if ($objectName != null)")
        }
        addEscapedStatement("""out.name("$key")""")

        // Since we are serializing nulls, we defer the if-statement until after the name is written.
        if (!isPrimitive && serializeNulls) {
            beginControlFlow("if ($objectName != null)")
        }

        val subTypeMetadata = value.subTypeMetadata
        val writeLine =
                if (subTypeMetadata != null) {
                    // If this field uses a subtype annotation, we use the type adapter subclasses instead of gson.
                    "${subTypeMetadata.getterName}().write(out, $objectName)"
                } else {
                    val adapterName: String = if (fieldTypeName is ParameterizedTypeName) {
                        // This is a generic type
                        "new com.google.gson.reflect.TypeToken<\$T>(){}"

                    } else {
                        "\$T.class"
                    }

                    // Otherwise we request the type adapter from gson.
                    "mGson.getAdapter($adapterName).write(out, $objectName)"
                }

        addStatement(writeLine, fieldTypeName.box())

        // If we are serializing nulls, we need to ensure we output it here.
        if (!isPrimitive) {
            if (serializeNulls) {
                nextControlFlow("else")
                addStatement("out.nullValue()")
            }
            endControlFlow()
        }
        addNewLine()

        return fieldCount + 1
    }
}