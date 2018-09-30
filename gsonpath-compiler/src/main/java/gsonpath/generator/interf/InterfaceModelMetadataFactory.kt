package gsonpath.generator.interf

import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.util.TypeHandler
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

class InterfaceModelMetadataFactory(private val typeHandler: TypeHandler) {

    fun createMetadata(element: TypeElement): List<InterfaceModelMetadata> {
        return typeHandler.getMethods(element).map { enclosedElement ->
            val methodType = enclosedElement.asType() as ExecutableType

            // Ensure that any generics have been converted into their actual return types.
            val returnTypeMirror: TypeMirror = (typeHandler.getGenerifiedTypeMirror(element, enclosedElement)
                    as ExecutableType).returnType
            val typeName = TypeName.get(returnTypeMirror)

            if (typeName == null || typeName == TypeName.VOID) {
                throw ProcessingException("Gson Path interface methods must have a return type", enclosedElement)
            }

            if (methodType.parameterTypes.isNotEmpty()) {
                throw ProcessingException("Gson Path interface methods must not have parameters", enclosedElement)
            }

            val methodName = enclosedElement.simpleName.toString()

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

            InterfaceModelMetadata(typeName, fieldName, enclosedElement, methodName, returnTypeMirror)
        }
    }
}