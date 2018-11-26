package gsonpath.extension.empty

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.addException
import gsonpath.extension.annotation.EmptyToNull
import gsonpath.util.ProcessorTypeHandler
import gsonpath.util.`if`
import gsonpath.util.assign
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeMirror

/**
 * A {@link GsonPathExtension} that supports the '@Size' annotation.
 */
class EmptyToNullGsonPathFieldValidator : GsonPathExtension {

    override val extensionName: String
        get() = "'EmptyToNull' Annotation"

    override fun canHandleFieldRead(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): Boolean {

        return false
    }

    override fun createCodeReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata,
            checkIfResultIsNull: Boolean): CodeBlock {

        throw UnsupportedOperationException("This extension does not handle reading")
    }

    override fun createCodePostReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        val (fieldInfo, variableName, jsonPath, isRequired) = extensionFieldMetadata

        if (fieldInfo.getAnnotation(EmptyToNull::class.java) == null) {
            return null
        }

        val fieldCollectionType: Boolean =
                try {
                    ProcessorTypeHandler(processingEnvironment).isMirrorOfCollectionType(fieldInfo.typeMirror)
                } catch (e: Exception) {
                    false
                }
        val fieldMapType: Boolean =
                try {
                    isFieldMapType(processingEnvironment, fieldInfo.typeMirror)
                } catch (e: Exception) {
                    false
                }

        val fieldType: FieldType =
                when {
                    (fieldInfo.typeMirror is ArrayType) -> FieldType.ARRAY
                    fieldCollectionType -> FieldType.COLLECTION
                    fieldMapType -> FieldType.MAP
                    (fieldInfo.typeName == ClassName.get(String::class.java)) -> FieldType.STRING

                    else ->
                        throw ProcessingException("Unexpected type found for field annotated with 'EmptyToNull', only " +
                                "string, array, map, or collection classes may be used.", fieldInfo.element)
                }

        return codeBlock {
            `if`("$variableName${fieldType.emptyCheck}") {
                if (isRequired) {
                    addException("JSON element '$jsonPath' cannot be blank")
                } else {
                    assign(variableName, "null")
                }
            }
        }
    }

    private fun isFieldMapType(processingEnv: ProcessingEnvironment, typeMirror: TypeMirror): Boolean {
        val mapTypeElement = processingEnv.elementUtils.getTypeElement(Map::class.java.name)
        val typeUtils = processingEnv.typeUtils
        val mapWildcardType = typeUtils.getDeclaredType(mapTypeElement,
                typeUtils.getWildcardType(null, null),
                typeUtils.getWildcardType(null, null))

        return typeUtils.isSubtype(typeMirror, mapWildcardType)
    }

    /**
     * Defines the type of field being used.
     *
     * The 'Size' annotation supports arrays and collections, and the generated code syntax must change depending
     * on which type is used.
     */
    enum class FieldType(val emptyCheck: String) {
        ARRAY(".length == 0"),
        COLLECTION(".size() == 0"),
        STRING(".trim().length() == 0"),
        MAP(".size() == 0")
    }
}
