package gsonpath.extension.def.intdef

import com.squareup.javapoet.CodeBlock
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.extension.addException
import gsonpath.extension.def.DefAnnotationMirrors
import gsonpath.extension.def.getDefAnnotationMirrors
import gsonpath.extension.getAnnotationValueObject
import gsonpath.util.addWithNewLine
import gsonpath.util.case
import gsonpath.util.codeBlock
import gsonpath.util.switch
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationValue

/**
 * A {@link GsonPathExtension} that supports the '@IntDef' annotation.
 */
class IntDefGsonPathFieldValidator : GsonPathExtension {
    override val extensionName: String
        get() = "'Int Def' Annotation"

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

        val (fieldInfo, variableName) = extensionFieldMetadata

        val defAnnotationMirrors: DefAnnotationMirrors = getDefAnnotationMirrors(fieldInfo.element,
                "android.support.annotation", "IntDef") ?: return null

        // The integer constants within the 'IntDef#values' property.
        val intDefValues: List<*> = getAnnotationValueObject(defAnnotationMirrors.defAnnotationMirror, "value")
                as List<*>? ?: return null

        return codeBlock {
            switch(variableName) {
                // Create a 'case' for each valid integer.
                intDefValues.forEach {
                    case((it as AnnotationValue).value.toString()) {}
                }

                // Create a 'default' that throws an exception if an unexpected integer is found.
                addWithNewLine("default:")
                indent()
                addException("""Unexpected Int '" + $variableName + "' for JSON element '${extensionFieldMetadata.jsonPath}'""")
                unindent()
            }
        }
    }

}
