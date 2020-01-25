package gsonpath.adapter.common

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.WildcardTypeName
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import javax.lang.model.element.TypeElement

class SubTypeGetterValidator {
    fun validateGsonSubtypeGetterMethod(
            classElement: TypeElement,
            methodContent: MethodElementContent): ProcessingException? {

        val elementTypeName = TypeName.get(classElement.asType())
        val expectedReturnType = ParameterizedTypeName.get(
                ClassName.get(Class::class.java), WildcardTypeName.subtypeOf(elementTypeName))

        return if (methodContent.returnTypeName != expectedReturnType) {
            ProcessingException("Incorrect return type for @GsonSubtypeGetter method. It must be " +
                    "Class<? extends ${classElement.simpleName}>", methodContent.element)
        } else {
            null
        }
    }
}