package gsonpath.extension.size

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.addException
import gsonpath.extension.getAnnotationMirror
import gsonpath.extension.getAnnotationValueObject
import gsonpath.util.ProcessorTypeHandler
import gsonpath.util.`if`
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.type.ArrayType

/**
 * A {@link GsonPathExtension} that supports the '@Size' annotation.
 */
class SizeGsonPathFieldValidator : GsonPathExtension {

    override val extensionName: String
        get() = "'Size' Annotation"

    override fun createCodePostReadCodeBlock(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): CodeBlock? {

        val (fieldInfo, variableName, jsonPath) = extensionFieldMetadata

        val sizeAnnotation: AnnotationMirror =
                getAnnotationMirror(fieldInfo.element, "android.support.annotation", "Size")
                        ?: getAnnotationMirror(fieldInfo.element, "gsonpath.extension.annotation", "Size")
                        ?: return null

        // Ensure that the field is either an array, string or a collection.
        val fieldCollectionType: Boolean =
                try {
                    ProcessorTypeHandler(processingEnvironment).isMirrorOfCollectionType(fieldInfo.typeMirror)
                } catch (e: Exception) {
                    false
                }
        val fieldType: FieldType =
                when {
                    (fieldInfo.typeMirror is ArrayType) -> FieldType.ARRAY
                    fieldCollectionType -> FieldType.COLLECTION
                    (fieldInfo.typeName == ClassName.get(String::class.java)) -> FieldType.STRING

                    else ->
                        throw ProcessingException("Unexpected type found for field annotated with 'Size', only " +
                                "arrays, string, or collection classes may be used.", fieldInfo.element)
                }

        val validationCodeBlock = codeBlock {
            handleExactLength(sizeAnnotation, jsonPath, variableName, fieldType)
            handleMin(sizeAnnotation, jsonPath, variableName, fieldType)
            handleMax(sizeAnnotation, jsonPath, variableName, fieldType)
            handleMultiple(sizeAnnotation, jsonPath, variableName, fieldType)
        }
        if (!validationCodeBlock.isEmpty) {
            return validationCodeBlock
        }
        return null
    }

    /**
     * Adds the size 'min value' validation if the minValue does not equal the floor-value.
     *
     * @param sizeAnnotation the annotation to obtain the size values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleMin(sizeAnnotation: AnnotationMirror, jsonPath: String,
                                            variableName: String, fieldType: FieldType): CodeBlock.Builder {

        val minValue: Long = getAnnotationValueObject(sizeAnnotation, "min") as Long? ?: return this

        if (minValue == java.lang.Long.MIN_VALUE) {
            return this
        }

        val lengthProperty = fieldType.lengthProperty
        return `if`("$variableName.$lengthProperty < $minValue") {
            addSizeException(fieldType, jsonPath,
                    """Expected minimum: '$minValue', actual minimum: '" + $variableName.$lengthProperty + "'""")
        }
    }

    /**
     * Adds the size 'max value' validation if the maxValue does not equal the ceiling-value.
     *
     * @param sizeAnnotation the annotation to obtain the size values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleMax(sizeAnnotation: AnnotationMirror, jsonPath: String, variableName: String,
                                            fieldType: FieldType): CodeBlock.Builder {

        val maxValue: Long = getAnnotationValueObject(sizeAnnotation, "max") as Long? ?: return this

        if (maxValue == java.lang.Long.MAX_VALUE) {
            return this
        }

        val lengthProperty = fieldType.lengthProperty
        return `if`("$variableName.$lengthProperty > $maxValue", variableName, maxValue) {
            addSizeException(fieldType, jsonPath,
                    """Expected maximum: '$maxValue', actual maximum: '" + $variableName.$lengthProperty + "'""")
        }
    }

    /**
     * Adds the size 'multiple' validation if the multipleValue does not equal 1.
     *
     * 'Multiple' means that the array/collection must have a size that is a multiple of this value.
     *
     * @param sizeAnnotation the annotation to obtain the size values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleMultiple(sizeAnnotation: AnnotationMirror, jsonPath: String,
                                                 variableName: String, fieldType: FieldType): CodeBlock.Builder {

        val multipleValue: Long = getAnnotationValueObject(sizeAnnotation, "multiple") as Long? ?: return this

        if (multipleValue == 1L) {
            return this
        }

        val lengthProperty = fieldType.lengthProperty
        return `if`("$variableName.$lengthProperty % $multipleValue != 0", variableName, multipleValue) {
            addSizeException(fieldType, jsonPath,
                    """$lengthProperty of '" + $variableName.$lengthProperty + "' is not a multiple of $multipleValue""")
        }
    }

    /**
     * Adds the size 'exact length' validation if the exactLengthValue does not equal -1.
     *
     * 'Exact length' means that the array/collection must have a size that matches this value.
     *
     * @param sizeAnnotation the annotation to obtain the size values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleExactLength(sizeAnnotation: AnnotationMirror, jsonPath: String,
                                                    variableName: String, fieldType: FieldType): CodeBlock.Builder {

        val exactLengthValue: Long = getAnnotationValueObject(sizeAnnotation, "value") as Long? ?: return this

        if (exactLengthValue == -1L) {
            return this
        }

        val lengthProperty = fieldType.lengthProperty
        return `if`("$variableName.$lengthProperty != $exactLengthValue", variableName, exactLengthValue) {
            addSizeException(fieldType, jsonPath, "Expected $lengthProperty: '$exactLengthValue', " +
                    """actual $lengthProperty: '" + $variableName.$lengthProperty + "'""")
        }
    }

    /**
     * Adds an exception that prepends an error message that is common across all of the 'Size' validations.
     */
    private fun CodeBlock.Builder.addSizeException(fieldType: FieldType, jsonPath: String,
                                                   exceptionText: String): CodeBlock.Builder {

        return addException("Invalid ${fieldType.label} ${fieldType.lengthProperty} for JSON element '$jsonPath'. " +
                exceptionText)
    }

    /**
     * Defines the type of field being used.
     *
     * The 'Size' annotation supports arrays and collections, and the generated code syntax must change depending
     * on which type is used.
     */
    enum class FieldType(val label: String, val lengthProperty: String) {
        ARRAY("array", "length"),
        COLLECTION("collection", "size()"),
        STRING("string", "length()");
    }
}
