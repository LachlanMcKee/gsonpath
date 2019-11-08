package gsonpath.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.sun.source.tree.VariableTree
import com.sun.source.util.TreePathScanner
import javax.lang.model.element.Element

interface DefaultValueDetector {
    fun hasDefaultValue(element: Element): Boolean
}

class DefaultValueDetectorImpl(private val sunTreesProvider: SunTreesProvider) : DefaultValueDetector {
    override fun hasDefaultValue(element: Element): Boolean {
        return DefaultValueScanner(element)
                .scan(sunTreesProvider.getTrees().getPath(element), null) != null
    }

    /**
     * Scans a field and detects whether a default value has been set.
     *
     * If a value has been set, the result will be an empty list, otherwise it will be null.
     */
    private class DefaultValueScanner(val fieldElement: Element) : TreePathScanner<List<String>?, Void>() {
        override fun visitVariable(node: VariableTree?, p: Void?): List<String>? {
            // Ignore default values for Kotlin classes (the stubs always set a default, but the real bytecode does not)
            if (isKotlinClass(fieldElement.enclosingElement)) {
                return null
            }
            return node?.initializer?.let { emptyList() }
        }

        private fun isKotlinClass(element: Element): Boolean {
            return element.annotationMirrors.any {
                TypeName.get(it.annotationType.asElement().asType()) == ClassName.get("kotlin", "Metadata")
            }
        }
    }
}