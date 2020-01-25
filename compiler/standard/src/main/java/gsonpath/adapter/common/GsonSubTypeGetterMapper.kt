package gsonpath.adapter.common

import gsonpath.GsonSubtypeGetter
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import gsonpath.util.TypeHandler
import javax.lang.model.element.TypeElement

class GsonSubTypeGetterMapper(private val typeHandler: TypeHandler) {
    fun mapElementToGetterMethod(classElement: TypeElement): MethodElementContent {
        return typeHandler.getMethods(classElement)
                .filter { it.element.getAnnotation(GsonSubtypeGetter::class.java) != null }
                .let {
                    when {
                        it.isEmpty() -> throw ProcessingException("An @GsonSubtypeGetter method must be defined. See the annotation for more information", classElement)
                        it.size > 1 -> throw ProcessingException("Only one @GsonSubtypeGetter method may exist", classElement)
                        else -> it.first()
                    }
                }
    }
}