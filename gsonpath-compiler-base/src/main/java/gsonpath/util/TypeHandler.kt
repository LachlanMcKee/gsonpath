package gsonpath.util

import com.squareup.javapoet.TypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

interface TypeHandler {
    fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean
    fun asElement(t: TypeMirror): Element?
    fun getAllMembers(typeElement: TypeElement): List<Element>
    fun getFields(typeElement: TypeElement, filterFunc: ((Element) -> Boolean)): List<Element>
    fun getMethods(element: TypeElement): List<Element>
    fun getGenerifiedTypeMirror(containing: TypeElement, element: Element): TypeMirror
    fun isMirrorOfCollectionType(typeMirror: TypeMirror): Boolean
}

class ProcessorTypeHandler(private val processingEnv: ProcessingEnvironment) : TypeHandler {
    override fun asElement(t: TypeMirror): Element? {
        return processingEnv.typeUtils.asElement(t)
    }

    override fun isSubtype(t1: TypeMirror, t2: TypeMirror): Boolean {
        return processingEnv.typeUtils.isSubtype(t1, t2)
    }

    override fun getAllMembers(typeElement: TypeElement): List<Element> {
        return processingEnv.elementUtils.getAllMembers(typeElement)
    }

    override fun getFields(typeElement: TypeElement, filterFunc: (Element) -> Boolean): List<Element> {
        return getAllMembers(typeElement)
                .asSequence()
                .filter {
                    // Ignore modelElement that are not fields.
                    it.kind == ElementKind.FIELD
                }
                .filter(filterFunc)
                .toList()
    }

    override fun getMethods(element: TypeElement): List<Element> {
        return getAllMembers(element)
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
                    !it.modifiers.contains(Modifier.DEFAULT) &&
                            !it.modifiers.contains(Modifier.STATIC)
                }
                .toList()
    }

    override fun getGenerifiedTypeMirror(containing: TypeElement, element: Element): TypeMirror {
        return processingEnv.typeUtils.asMemberOf(containing.asType() as DeclaredType, element)
    }

    override fun isMirrorOfCollectionType(typeMirror: TypeMirror): Boolean {
        val rawType: TypeMirror = when (typeMirror) {
            is DeclaredType -> typeMirror.typeArguments.first()

            else -> return false
        }

        val collectionTypeElement = processingEnv.elementUtils.getTypeElement(Collection::class.java.name)
        val collectionType = processingEnv.typeUtils.getDeclaredType(collectionTypeElement, rawType)

        return processingEnv.typeUtils.isSubtype(typeMirror, collectionType)
    }
}