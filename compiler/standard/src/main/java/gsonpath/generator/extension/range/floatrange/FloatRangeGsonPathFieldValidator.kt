package gsonpath.generator.extension.range.floatrange

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import gsonpath.ProcessingException
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.generator.extension.getAnnotationMirror
import gsonpath.generator.extension.getAnnotationValueObject
import gsonpath.generator.extension.range.handleRangeValue
import gsonpath.model.FieldType
import gsonpath.util.codeBlock
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror

/**
 * A {@link GsonPathExtension} that supports the '@FloatRange' annotation.
 */
class FloatRangeGsonPathFieldValidator : GsonPathExtension {
    private val BOXED_FLOAT = ClassName.get("java.lang", "Float")
    private val BOXED_DOUBLE = ClassName.get("java.lang", "Double")

    override val extensionName: String
        get() = "'FloatRange' Annotation"

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        val (fieldInfo, variableName, jsonPath) = extensionFieldMetadata

        val floatRangeAnnotation: AnnotationMirror =
                getAnnotationMirror(fieldInfo.element, "android.support.annotation", "FloatRange")
                        ?: getAnnotationMirror(fieldInfo.element, "gsonpath.extension.annotation", "FloatRange")
                        ?: return null

        // Ensure that the field is either a float, or a double.
        val typeName = fieldInfo.fieldType.typeName.let {
            if (fieldInfo.fieldType is FieldType.Primitive) {
                it.box()
            } else {
                it
            }
        }

        if (typeName != BOXED_DOUBLE && typeName != BOXED_FLOAT) {
            throw ProcessingException("Unexpected type found for field annotated with 'FloatRange', only " +
                    "floats and doubles are allowed.", fieldInfo.element)
        }

        val validationCodeBlock = codeBlock {
            handleFrom(floatRangeAnnotation, jsonPath, variableName)
            handleTo(floatRangeAnnotation, jsonPath, variableName)
        }
        if (!validationCodeBlock.isEmpty) {
            return GsonPathExtension.ExtensionResult(validationCodeBlock)
        }
        return null
    }

    /**
     * Adds the range 'from' validation if the fromValue does not equal the floor-value.
     *
     * @param floatRangeAnnotationMirror the annotation to obtain the range values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleFrom(floatRangeAnnotationMirror: AnnotationMirror, jsonPath: String,
                                             variableName: String): CodeBlock.Builder {

        val fromValue: Double = getAnnotationValueObject(floatRangeAnnotationMirror, "from") as Double? ?: return this
        val fromInclusive: Boolean = getAnnotationValueObject(floatRangeAnnotationMirror, "fromInclusive") as Boolean?
                ?: true

        if (fromValue == Double.NEGATIVE_INFINITY) {
            return this
        }

        return handleRangeValue(fromValue.toString(), true, fromInclusive, jsonPath, variableName)
    }

    /**
     * Adds the range 'to' validation if the toValue does not equal the ceiling-value.
     *
     * @param floatRangeAnnotationMirror the annotation to obtain the range values
     * @param jsonPath the json path of the field being validated
     * @param variableName the name of the variable that is assigned back to the fieldName
     */
    private fun CodeBlock.Builder.handleTo(floatRangeAnnotationMirror: AnnotationMirror, jsonPath: String,
                                           variableName: String): CodeBlock.Builder {

        val toValue: Double = getAnnotationValueObject(floatRangeAnnotationMirror, "to") as Double? ?: return this
        val toInclusive: Boolean = getAnnotationValueObject(floatRangeAnnotationMirror, "toInclusive") as Boolean?
                ?: true

        if (toValue == java.lang.Double.POSITIVE_INFINITY) {
            return this
        }

        return handleRangeValue(toValue.toString(), false, toInclusive, jsonPath, variableName)
    }
}
