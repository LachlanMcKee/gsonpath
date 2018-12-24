package gsonpath.extension.empty

import com.squareup.javapoet.ClassName
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.addException
import gsonpath.extension.annotation.EmptyToNull
import gsonpath.model.FieldType
import gsonpath.util.`if`
import gsonpath.util.assign
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment

/**
 * A {@link GsonPathExtension} that supports the '@Size' annotation.
 */
class EmptyToNullGsonPathFieldValidator : GsonPathExtension {

    override val extensionName: String
        get() = "'EmptyToNull' Annotation"

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        val (fieldInfo, variableName, jsonPath, isRequired) = extensionFieldMetadata

        if (fieldInfo.getAnnotation(EmptyToNull::class.java) == null) {
            return null
        }

        val emptyToNullFieldType: EmptyToNullFieldType =
                when {
                    fieldInfo.fieldType is FieldType.MultipleValues.Array -> EmptyToNullFieldType.ARRAY
                    fieldInfo.fieldType is FieldType.MultipleValues.Collection -> EmptyToNullFieldType.COLLECTION
                    fieldInfo.fieldType is FieldType.MapFieldType -> EmptyToNullFieldType.MAP
                    (fieldInfo.fieldType.typeName == ClassName.get(String::class.java)) -> EmptyToNullFieldType.STRING

                    else ->
                        throw ProcessingException("Unexpected type found for field annotated with 'EmptyToNull', only " +
                                "string, array, map, or collection classes may be used.", fieldInfo.element)
                }

        return GsonPathExtension.ExtensionResult(codeBlock {
            `if`("$variableName${emptyToNullFieldType.emptyCheck}") {
                if (isRequired) {
                    addException("JSON element '$jsonPath' cannot be blank")
                } else {
                    assign(variableName, "null")
                }
            }
        })
    }

    /**
     * Defines the type of field being used.
     *
     * The 'Size' annotation supports arrays and collections, and the generated code syntax must change depending
     * on which type is used.
     */
    enum class EmptyToNullFieldType(val emptyCheck: String) {
        ARRAY(".length == 0"),
        COLLECTION(".size() == 0"),
        STRING(".trim().length() == 0"),
        MAP(".size() == 0")
    }
}
