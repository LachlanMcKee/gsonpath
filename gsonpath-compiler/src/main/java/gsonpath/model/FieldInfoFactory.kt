package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.ExcludeField
import gsonpath.ProcessingException
import gsonpath.RepeatableSerializedName
import gsonpath.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class FieldInfoFactory(
        private val typeHandler: TypeHandler,
        private val fieldGetterFinder: FieldGetterFinder,
        private val annotationFetcher: AnnotationFetcher,
        private val defaultValueDetector: DefaultValueDetector) {

    class InterfaceInfo(
            val parentClassName: ClassName,
            val fieldInfo: List<InterfaceFieldInfo>)

    class InterfaceFieldInfo(
            val elementInfo: ElementInfo,
            val typeName: TypeName,
            val typeMirror: TypeMirror,
            val fieldName: String,
            val getterMethodName: String)

    interface ElementInfo {
        val underlyingElement: Element

        fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T?

        val annotationNames: List<String>
    }

    /**
     * Obtain all possible elements contained within the annotated class, including inherited fields.
     */
    fun getModelFieldsFromElement(
            modelElement: TypeElement,
            fieldsRequireAnnotation: Boolean,
            useConstructor: Boolean): List<FieldInfo> {

        val filterFunc: (Element) -> Boolean = {

            // Ignore static and transient fields.
            !(it.modifiers.contains(Modifier.STATIC) || it.modifiers.contains(Modifier.TRANSIENT)) &&

                    // If a field is final, we only add it if we are using a constructor to assign it.
                    (!it.modifiers.contains(Modifier.FINAL) || useConstructor) &&

                    (!fieldsRequireAnnotation || it.getAnnotationEx(SerializedName::class.java) != null ||
                            it.getAnnotationEx(RepeatableSerializedName::class.java) != null) &&

                    // Ignore any excluded fields
                    it.getAnnotationEx(ExcludeField::class.java) == null
        }
        return typeHandler.getFields(modelElement, filterFunc)
                .map { (memberElement, generifiedElement) ->
                    object : FieldInfo {
                        override val typeName: TypeName
                            get() = TypeName.get(generifiedElement)

                        override val typeMirror: TypeMirror
                            get() = generifiedElement

                        override val parentClassName: String
                            get() = memberElement.enclosingElement.toString()

                        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
                            return annotationFetcher.getAnnotation(modelElement, memberElement, annotationClass)
                        }

                        override val fieldName: String
                            get() = memberElement.simpleName.toString()

                        override val fieldAccessor: String
                            get() {
                                return if (!memberElement.modifiers.contains(Modifier.PRIVATE)) {
                                    memberElement.simpleName.toString()
                                } else {
                                    findFieldGetterMethodName(modelElement, memberElement) + "()"
                                }
                            }

                        override val annotationNames: List<String>
                            get() {
                                return annotationFetcher.getAnnotationMirrors(modelElement, memberElement)
                                        .map { it.annotationType.asElement().simpleName.toString() }
                            }

                        override val element: Element
                            get() = memberElement

                        override val hasDefaultValue: Boolean
                            get() = defaultValueDetector.hasDefaultValue(memberElement)
                    }
                }
    }

    /**
     * Attempts to find a logical getter method name for a variable.
     *
     * @param modelElement the parent element of the field.
     * @param variableElement the field element we want to find the getter method for.
     */
    private fun findFieldGetterMethodName(modelElement: TypeElement, variableElement: Element): String {
        val method = fieldGetterFinder.findGetter(modelElement, variableElement)
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
