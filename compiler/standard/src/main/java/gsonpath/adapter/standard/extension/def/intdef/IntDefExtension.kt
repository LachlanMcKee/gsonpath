package gsonpath.adapter.standard.extension.def.intdef

import gsonpath.adapter.standard.extension.addException
import gsonpath.adapter.standard.extension.def.DefAnnotationMirrors
import gsonpath.adapter.standard.extension.def.getDefAnnotationMirrors
import gsonpath.adapter.standard.extension.getAnnotationValueObject
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.addWithNewLine
import gsonpath.util.case
import gsonpath.util.codeBlock
import gsonpath.util.switch
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationValue

/**
 * A {@link GsonPathExtension} that supports the '@IntDef' annotation.
 */
class IntDefExtension : GsonPathExtension {
    override val extensionName: String
        get() = "'Int Def' Annotation"

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val defAnnotationMirrors: DefAnnotationMirrors = getDefAnnotationMirrors(fieldInfo.element,
                "android.support.annotation", "IntDef") ?: return null

        // The integer constants within the 'IntDef#values' property.
        val intDefValues: List<*> = getAnnotationValueObject(defAnnotationMirrors.defAnnotationMirror, "value")
                as List<*>? ?: return null

        return GsonPathExtension.ExtensionResult(codeBlock {
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
        })
    }

}
