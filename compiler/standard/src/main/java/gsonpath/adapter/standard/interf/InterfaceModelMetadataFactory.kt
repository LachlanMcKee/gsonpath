package gsonpath.adapter.standard.interf

import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import gsonpath.util.TypeHandler
import javax.lang.model.element.TypeElement

class InterfaceModelMetadataFactory(private val typeHandler: TypeHandler) {

    fun createMetadata(classElement: TypeElement): List<InterfaceModelMetadata> {
        return typeHandler.getMethods(classElement).map(::createMetadata)
    }

    private fun createMetadata(methodElementContent: MethodElementContent): InterfaceModelMetadata {
        val methodElement = methodElementContent.element

        val typeName = methodElementContent.returnTypeName
        if (typeName == TypeName.VOID) {
            throw ProcessingException("Gson Path interface methods must have a return type", methodElement)
        }

        if (methodElementContent.parameterElementContents.isNotEmpty()) {
            throw ProcessingException("Gson Path interface methods must not have parameters", methodElement)
        }

        return InterfaceModelMetadata(typeName, methodElementContent.getFieldName(), methodElement,
                methodElementContent.methodName, methodElementContent.returnTypeMirror)
    }

    /**
     * Transform the method name into the field name by removing the first camel-cased portion.
     * e.g. 'getName' becomes 'name'
     */
    private fun MethodElementContent.getFieldName(): String {
        return methodName.indexOfFirst(Char::isUpperCase)
                .takeIf { it != -1 }
                ?.let { methodName[it].toLowerCase() + methodName.substring(it + 1) }
                ?: methodName
    }
}