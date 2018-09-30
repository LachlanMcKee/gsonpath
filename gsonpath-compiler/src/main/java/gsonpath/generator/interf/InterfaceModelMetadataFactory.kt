package gsonpath.generator.interf

import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import gsonpath.util.TypeHandler
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

        val methodName = methodElement.simpleName.toString()

        //
        // Transform the method name into the field name by removing the first camel-cased portion.
        // e.g. 'getName' becomes 'name'
        //
        val fieldName: String = methodName.indexOfFirst(Char::isUpperCase)
                .let { upperCaseIndex ->
                    if (upperCaseIndex != -1) {
                        methodName[upperCaseIndex].toLowerCase() + methodName.substring(upperCaseIndex + 1)
                    } else {
                        methodName
                    }
                }

        return InterfaceModelMetadata(typeName, fieldName, methodElement, methodName, returnTypeMirror)
    }
}