package gsonpath.adapter.standard.extension.def.stringdef

import com.sun.source.tree.AnnotationTree
import com.sun.source.tree.AssignmentTree
import com.sun.source.tree.IdentifierTree
import com.sun.source.tree.MemberSelectTree
import com.sun.source.util.TreePathScanner
import com.sun.source.util.Trees
import com.sun.tools.javac.tree.JCTree
import gsonpath.adapter.standard.extension.addException
import gsonpath.adapter.standard.extension.def.DefAnnotationMirrors
import gsonpath.adapter.standard.extension.def.getDefAnnotationMirrors
import gsonpath.adapter.standard.extension.getAnnotationValue
import gsonpath.compiler.ExtensionFieldMetadata
import gsonpath.compiler.GsonPathExtension
import gsonpath.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationValue

/**
 * A {@link GsonPathExtension} that supports the '@StringDef' annotation.
 *
 * This extension has a quirk in which the string value must be overridden with the actual constant from the
 * '@StringDef` annotation.
 *
 * This is due to how the lint validation works in Android Studio. The linting requires that the exact same String
 * reference is used, rather than another String instance that has the same equality.
 */
class StringDefExtension : GsonPathExtension {

    override val extensionName: String
        get() = "'String Def' Annotation"

    override fun createCodePostReadResult(
            processingEnvironment: ProcessingEnvironment,
            extensionFieldMetadata: ExtensionFieldMetadata): GsonPathExtension.ExtensionResult? {

        val (fieldInfo, variableName) = extensionFieldMetadata

        val defAnnotationMirrors: DefAnnotationMirrors = getDefAnnotationMirrors(fieldInfo.element,
                "android.support.annotation", "StringDef") ?: return null

        // The annotation values reference which contains the String constants.
        val defAnnotationValues: AnnotationValue = getAnnotationValue(defAnnotationMirrors.defAnnotationMirror,
                "value") ?: return null

        //
        // The standard javax AnnotationMirror does not expose the ability to retrieve the actual constant variable
        // names, as it normally returns the value of the String. Therefore we use the 'TreePathScanner' to inspect
        // the actual source code and obtain the variable names.
        //
        val annotationElement = defAnnotationMirrors.annotationMirror.annotationType.asElement()
        val treesInstance = Trees.instance(processingEnvironment)
        val stringDefConstants: List<String>? = AnnotationValueConstantsVisitor().scan(
                treesInstance.getPath(annotationElement, defAnnotationMirrors.defAnnotationMirror, defAnnotationValues),
                null)

        // If there are no String constants in the 'StringDef' abort.
        if (stringDefConstants == null || stringDefConstants.isEmpty()) {
            return null
        }

        return GsonPathExtension.ExtensionResult(codeBlock {
            switch(variableName) {
                stringDefConstants
                        .map {
                            if (!it.contains(".")) {
                                // Append the enclosing class name
                                "$annotationElement.$it"
                            } else {
                                it
                            }
                        }
                        .forEach {
                            //
                            // The StringDef annotation requires the String constant to be referenced, therefore we must
                            // reassign the String value to the constant.
                            //
                            case(it) {
                                assign(variableName, it)
                            }
                        }

                // Throw an exception if an unexpected String is found.
                addWithNewLine("default:")
                indent()
                addException("""Unexpected String '" + $variableName + "' for JSON element '${extensionFieldMetadata.jsonPath}'""")
                unindent()
            }
        })
    }

    /**
     * Obtains the String constant variable name from the annotation rather than the String value itself.
     *
     * This is required due to the StringDef annotation requiring the String reference to be used and not a different
     * String with the same equality.
     */
    private class AnnotationValueConstantsVisitor : TreePathScanner<List<String>?, Void>() {

        override fun visitAnnotation(node: AnnotationTree, p: Void?): List<String>? {
            for (expressionTree in node.arguments) {
                if (expressionTree !is AssignmentTree) {
                    continue
                }

                // Find the 'value' property of the annotation.
                val variable = expressionTree.variable
                if (variable !is IdentifierTree || !variable.name.contentEquals("value")) {
                    continue
                }

                // Obtain the String constant variable names.
                val expression = expressionTree.expression as JCTree.JCNewArray
                return expression.elems.map(JCTree.JCExpression::toString)
            }

            return null
        }

        override fun visitAssignment(at: AssignmentTree, p: Void?): List<String>? {
            return null
        }

        override fun visitMemberSelect(mst: MemberSelectTree, p: Void?): List<String>? {
            return null
        }

        override fun visitIdentifier(it: IdentifierTree, p: Void?): List<String>? {
            return null
        }
    }

}
