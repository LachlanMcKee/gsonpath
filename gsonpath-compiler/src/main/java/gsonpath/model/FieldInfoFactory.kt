package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import gsonpath.ExcludeField
import gsonpath.ProcessingException
import gsonpath.util.DefaultValueDetector
import gsonpath.util.TypeHandler
import javax.lang.model.element.*
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.NoType
import javax.lang.model.type.TypeMirror

class FieldInfoFactory(private val typeHandler: TypeHandler, private val defaultValueDetector: DefaultValueDetector) {

    /**
     * Obtain all possible elements contained within the annotated class, including inherited fields.
     */
    fun getModelFieldsFromElement(modelElement: TypeElement, fieldsRequireAnnotation: Boolean, useConstructor: Boolean): List<FieldInfo> {
        val allMembers = typeHandler.getAllMembers(modelElement)

        return allMembers
                .filter {
                    // Ignore modelElement that are not fields.
                    it.kind == ElementKind.FIELD
                }
                .filter {
                    // Ignore static and transient fields.
                    val modifiers = it.modifiers
                    !(modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.TRANSIENT))
                }
                .filter {
                    // If a field is final, we only add it if we are using a constructor to assign it.
                    !it.modifiers.contains(Modifier.FINAL) || useConstructor
                }
                .filter {
                    !fieldsRequireAnnotation || it.getAnnotation(SerializedName::class.java) != null
                }
                .filter {
                    // Ignore any excluded fields
                    it.getAnnotation(ExcludeField::class.java) == null
                }
                .map { memberElement ->
                    // Ensure that any generics have been converted into their actual class.
                    val generifiedElement = typeHandler.getGenerifiedTypeMirror(modelElement, memberElement)

                    object : FieldInfo {
                        override val typeName: TypeName
                            get() = TypeName.get(generifiedElement)

                        override val typeMirror: TypeMirror
                            get() = generifiedElement

                        override val parentClassName: String
                            get() = memberElement.enclosingElement.toString()

                        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                            val memberAnnotation = memberElement.getAnnotation(annotationClass)
                            if (memberAnnotation != null) {
                                return memberAnnotation
                            }
                            return findMethodAnnotation(modelElement, memberElement, annotationClass)
                        }

                        override val fieldName: String
                            get() = memberElement.simpleName.toString()

                        override val fieldAccessor: String
                            get() {
                                return if (!memberElement.modifiers.contains(Modifier.PRIVATE)) {
                                    memberElement.simpleName.toString()
                                } else {
                                    findFieldGetterMethodName(allMembers, memberElement) + "()"
                                }
                            }

                        override val annotationNames: List<String>
                            get() {
                                return memberElement.annotationMirrors
                                        .plus(getMethodAnnotationMirrors(modelElement, memberElement))
                                        .map { it ->
                                            it.annotationType.asElement().simpleName.toString()
                                        }
                            }

                        override val element: Element
                            get() = memberElement

                        override val hasDefaultValue: Boolean
                            get() {
                                return defaultValueDetector.hasDefaultValue(memberElement)
                            }
                    }
                }
    }

    private fun <T : Annotation> findMethodAnnotation(
            modelElement: TypeElement?,
            memberElement: Element,
            annotationClass: Class<T>): T? {

        if (modelElement != null && modelElement !is NoType) {
            val annotation = findFieldGetterMethod(typeHandler.getAllMembers(modelElement), memberElement)
                    ?.getAnnotation(annotationClass)

            if (annotation != null) {
                return annotation
            }

            return findMethodAnnotation(typeHandler.asElement(modelElement.superclass) as? TypeElement,
                    memberElement, annotationClass)
        }
        return null
    }

    private fun getMethodAnnotationMirrors(modelElement: TypeElement?, memberElement: Element): List<AnnotationMirror> {
        return if (modelElement != null && modelElement !is NoType) {
            val annotationMirrors = findFieldGetterMethod(typeHandler.getAllMembers(modelElement), memberElement)
                    ?.annotationMirrors ?: emptyList()

            val superElement = typeHandler.asElement(modelElement.superclass)
            annotationMirrors.plus(getMethodAnnotationMirrors(superElement as? TypeElement, memberElement))
        } else {
            emptyList()
        }
    }

    /**
     * Attempts to find a logical getter method for a variable.
     *
     * For example, the following getter method names are valid for a variable named 'foo':
     * 'foo()', 'isFoo()', 'hasFoo()', 'getFoo()'
     *
     * If no getter method is found, an exception will be fired.
     *
     * @param allMembers all elements within the class.
     * @param variableElement the field element we want to find the getter method for.
     */
    private fun findFieldGetterMethod(allMembers: List<Element>, variableElement: Element): Element? {
        return allMembers
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

    /**
     * Attempts to find a logical getter method name for a variable.
     *
     * @see findFieldGetterMethod
     *
     * @param allMembers all elements within the class.
     * @param variableElement the field element we want to find the getter method for.
     */
    private fun findFieldGetterMethodName(allMembers: List<Element>, variableElement: Element): String {
        val method = findFieldGetterMethod(allMembers, variableElement)
                ?: throw ProcessingException("Unable to find getter for private variable", variableElement)

        return method.simpleName.toString()
    }

    fun getModelFieldsFromInterface(interfaceInfo: InterfaceInfo): List<FieldInfo> {
        return interfaceInfo.fieldInfo.map {
            object : FieldInfo {
                override val typeName: TypeName
                    get() = it.typeName

                override val typeMirror: TypeMirror
                    get() = it.typeMirror

                override val parentClassName: String
                    get() = interfaceInfo.parentClassName.toString()

                override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                    return it.elementInfo.getAnnotation(annotationClass)
                }

                override val fieldName: String
                    get() = it.fieldName

                override val fieldAccessor: String
                    get() = it.getterMethodName + "()"

                override val annotationNames: List<String>
                    get() = it.elementInfo.annotationNames

                override val element: Element
                    get() = it.elementInfo.underlyingElement

                override val hasDefaultValue: Boolean
                    get() = false
            }
        }
    }
}
