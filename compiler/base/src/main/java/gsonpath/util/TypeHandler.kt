package gsonpath.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

interface TypeHandler {
    fun getTypeName(typeMirror: TypeMirror): TypeName?
    fun getClassName(typeMirror: TypeMirror): TypeName?
    fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean
    fun asElement(t: TypeMirror): Element?
    fun getAllMembers(typeElement: TypeElement): List<Element>
    fun getFields(typeElement: TypeElement, filterFunc: ((Element) -> Boolean)): List<FieldElementContent>
    fun getMethods(typeElement: TypeElement): List<MethodElementContent>
    fun getDeclaredType(clazz: KClass<*>, vararg typeMirrors: TypeMirror): TypeMirror
    fun getWildcardType(extendsBound: TypeMirror?, superBound: TypeMirror?): TypeMirror
}

data class FieldElementContent(
        val element: Element,
        val generifiedElement: TypeMirror
)

data class MethodElementContent(
        val element: Element,
        val generifiedElement: ExecutableType
)

class ProcessorTypeHandler(private val processingEnv: ProcessingEnvironment) : TypeHandler {
    override fun getTypeName(typeMirror: TypeMirror): TypeName? = TypeName.get(typeMirror)

    override fun getClassName(typeMirror: TypeMirror): TypeName? = ClassName.get(typeMirror)

    override fun asElement(t: TypeMirror): Element? {
        return processingEnv.typeUtils.asElement(t)
    }

    override fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean {
        return processingEnv.typeUtils.isSubtype(t1, t2)
    }

    override fun getAllMembers(typeElement: TypeElement): List<Element> {
        return processingEnv.elementUtils.getAllMembers(typeElement)
    }

    override fun getFields(typeElement: TypeElement, filterFunc: (Element) -> Boolean): List<FieldElementContent> {
        return getAllMembers(typeElement)
                .asSequence()
                .filter(filterFunc)
                .map { FieldElementContent(it, getGenerifiedTypeMirror(typeElement, it)) }
                .toList()
    }

    override fun getMethods(typeElement: TypeElement): List<MethodElementContent> {
        return getAllMembers(typeElement)
                .asSequence()
                .filter {
                    // Ignore methods from the base Object class
                    TypeName.get(it.enclosingElement.asType()) != TypeName.OBJECT
                }
                .filter {
                    it.kind == ElementKind.METHOD
                }
                .filter {
                    // Ignore Java 8 default/static interface methods.
                    if (typeElement.kind == ElementKind.INTERFACE) {
                        !it.modifiers.contains(Modifier.DEFAULT) &&
                                !it.modifiers.contains(Modifier.STATIC)
                    } else {
                        true
                    }
                }
                .map { MethodElementContent(it, getGenerifiedTypeMirror(typeElement, it) as ExecutableType) }
                .toList()
    }

    override fun getDeclaredType(clazz: KClass<*>, vararg typeMirrors: TypeMirror): TypeMirror {
        val typeElement = processingEnv.elementUtils.getTypeElement(clazz.java.name)
        return processingEnv.typeUtils.getDeclaredType(typeElement, *typeMirrors)
    }

    override fun getWildcardType(extendsBound: TypeMirror?, superBound: TypeMirror?): TypeMirror {
        return processingEnv.typeUtils.getWildcardType(extendsBound, superBound)
    }

    private fun getGenerifiedTypeMirror(containing: TypeElement, element: Element): TypeMirror {
        return processingEnv.typeUtils.asMemberOf(containing.asType() as DeclaredType, element)
    }
}