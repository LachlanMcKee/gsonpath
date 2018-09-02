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
import gsonpath.util.MethodSpecExt
import gsonpath.util.addComment
import gsonpath.util.addEscapedStatement
import gsonpath.util.addNewLine
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

        val writeMethod = MethodSpecExt.interfaceMethodBuilder("write")
                .addParameter(JsonWriter::class.java, "out")
                .addParameter(elementClassName, "value")
                .addException(IOException::class.java)

        val codeBlock = CodeBlock.builder()

                // Initial block which prevents nulls being accessed.
                .beginControlFlow("if (value == null)")
                .addStatement("out.nullValue()")
                .addStatement("return")
                .endControlFlow()

                .addNewLine()
                .addComment("Begin")

        writeGsonFieldWriter(codeBlock, rootElements, "", serializeNulls, 0)

        writeMethod.addCode(codeBlock.build())
        return writeMethod.build()
    }

    @Throws(ProcessingException::class)
    private fun writeGsonFieldWriter(
            codeBlock: CodeBlock.Builder,
            jsonMapping: GsonObject,
            currentPath: String,
            serializeNulls: Boolean,
            currentFieldCount: Int): Int {

        codeBlock.addStatement("out.beginObject()")

        val overallFieldCount: Int = jsonMapping.entries().fold(currentFieldCount) { fieldCount, (key, value) ->
            when (value) {
                is GsonField -> {
                    val fieldInfo = value.fieldInfo

                    // Make sure the field's annotations don't have any problems.
                    SharedFunctions.validateFieldAnnotations(fieldInfo)

                    val fieldTypeName = fieldInfo.typeName
                    val isPrimitive = fieldTypeName.isPrimitive

                    val objectName = "obj$fieldCount"

                    codeBlock.addStatement("\$T $objectName = value.${fieldInfo.fieldAccessor}", fieldTypeName)

                    // If we aren't serializing nulls, we need to prevent the 'out.name' code being executed.
                    if (!isPrimitive && !serializeNulls) {
                        codeBlock.beginControlFlow("if ($objectName != null)")
                    }
                    codeBlock.addEscapedStatement("""out.name("$key")""")

                    // Since we are serializing nulls, we defer the if-statement until after the name is written.
                    if (!isPrimitive && serializeNulls) {
                        codeBlock.beginControlFlow("if ($objectName != null)")
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

                    codeBlock.addStatement(writeLine, fieldTypeName.box())

                    // If we are serializing nulls, we need to ensure we output it here.
                    if (!isPrimitive) {
                        if (serializeNulls) {
                            codeBlock.nextControlFlow("else")
                                    .addStatement("out.nullValue()")
                        }
                        codeBlock.endControlFlow()
                    }
                    codeBlock.addNewLine()

                    return@fold fieldCount + 1
                }

                is GsonObject -> {
                    if (value.size() > 0) {
                        val newPath: String = if (currentPath.isNotEmpty()) {
                            currentPath + "" + key
                        } else {
                            key
                        }

                        // Add a comment mentioning what nested object we are current pointing at.
                        codeBlock.addNewLine()
                                .addComment("Begin $newPath")
                                .addStatement("""out.name("$key")""")

                        return@fold writeGsonFieldWriter(codeBlock, value, newPath, serializeNulls, fieldCount)
                    }
                }
            }

            // The count has not changed
            return@fold fieldCount
        }

        codeBlock.addComment("End $currentPath")
                .addStatement("out.endObject()")

        return overallFieldCount
    }
}