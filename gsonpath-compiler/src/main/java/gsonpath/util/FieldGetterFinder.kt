package gsonpath.util

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

class FieldGetterFinder(private val typeHandler: TypeHandler) {
    /**
     * Attempts to find a logical getter method for a variable.
     *
     * For example, the following getter method names are valid for a variable named 'foo':
     * 'foo()', 'isFoo()', 'hasFoo()', 'getFoo()'
     *
     * If no getter method is found, an exception will be fired.
     *
     * @param parentElement the parent element of the field.
     * @param variableElement the field element we want to find the getter method for.
     */
    fun findGetter(parentElement: TypeElement, variableElement: Element): Element? {
        return typeHandler.getAllMembers(parentElement)
                .filter { it.kind == ElementKind.METHOD }
                .filter {
                    // See if the method name either matches the variable name, or starts with a standard getter prefix.
                    val remainder = it.simpleName.toString()
                            .toLowerCase()
                            .replace(variableElement.simpleName.toString().toLowerCase(), "")
                    arrayOf("", "is", "has", "get").contains(remainder)
                }
                .find { (it.asType() as ExecutableType).parameterTypes.size == 0 }
    }
}