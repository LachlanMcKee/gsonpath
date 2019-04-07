package gsonpath.adapter.standard.interf

import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import gsonpath.util.TypeHandler
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

class InterfaceModelMetadataFactory(private val typeHandler: TypeHandler) {

    fun createMetadata(classElement: TypeElement): List<InterfaceModelMetadata> {
        return typeHandler.getMethods(classElement).map(::createMetadata)
    }

    private fun createMetadata(methodElementContent: MethodElementContent): InterfaceModelMetadata {
        val methodElement = methodElementContent.element

        // Ensure that any generics have been converted into their actual return types.
        val returnTypeMirror: TypeMirror = methodElementContent.generifiedElement.returnType
        val typeName = typeHandler.getTypeName(returnTypeMirror)

        if (typeName == null || typeName == TypeName.VOID) {
            throw ProcessingException("Gson Path interface methods must have a return type", methodElement)
        }

        (methodElement.asType() as ExecutableType).let {
            if (it.parameterTypes.isNotEmpty()) {
                throw ProcessingException("Gson Path interface methods must not have parameters", methodElement)
            }
        }

        return InterfaceModelMetadata(typeName, methodElement.getFieldName(), methodElement,
                methodElement.getMethodName(), returnTypeMirror)
    }

    private fun Element.getMethodName() = simpleName.toString()

    /**
     * Transform the method name into the field name by removing the first camel-cased portion.
     * e.g. 'getName' becomes 'name'
     */
    private fun Element.getFieldName() = getMethodName().let { methodName ->
        methodName.indexOfFirst(Char::isUpperCase)
                .takeIf { it != -1 }
                ?.let { methodName[it].toLowerCase() + methodName.substring(it + 1) }
                ?: methodName
    }
}