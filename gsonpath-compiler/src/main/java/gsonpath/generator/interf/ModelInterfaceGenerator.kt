package gsonpath.generator.interf

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.compiler.generateClassName
import gsonpath.generator.writeFile
import gsonpath.model.FieldInfoFactory
import gsonpath.model.FieldInfoFactory.InterfaceFieldInfo
import gsonpath.model.FieldInfoFactory.InterfaceInfo
import gsonpath.util.*
import javax.annotation.Generated
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror

class ModelInterfaceGenerator(
        private val typeHandler: TypeHandler,
        private val fileWriter: FileWriter,
        private val logger: Logger) {

    @Throws(ProcessingException::class)
    fun handle(element: TypeElement): InterfaceInfo {
        return createOutputClassName(element).let { outputClassName ->
            TypeSpecExt.finalClassBuilder(outputClassName)
                    .addDetails(element, outputClassName)
        }
    }

    private fun TypeSpec.Builder.addDetails(element: TypeElement, outputClassName: ClassName): InterfaceInfo {
        addSuperinterface(ClassName.get(element))
        addAnnotation(AnnotationSpec.builder(Generated::class.java).run {
            addMember("value", "\"gsonpath.GsonProcessor\"")
            addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
            build()
        })

        val modelElementDetails = createModelElementDetails(element, getMethodElements(element))

        addFields(modelElementDetails)
        addConstructor(modelElementDetails)
        addGetters(modelElementDetails)
        addEqualsMethod(outputClassName, modelElementDetails)
        addHashCodeMethod(modelElementDetails)
        addToStringMethod(element, modelElementDetails)

        if (!writeFile(fileWriter, logger, outputClassName.packageName())) {
            throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
        }

        return InterfaceInfo(outputClassName, modelElementDetails.map {
            InterfaceFieldInfo(StandardElementInfo(it.enclosedElement),
                    it.typeName, it.returnTypeMirror, it.fieldName, it.methodName)
        })
    }

    private fun TypeSpec.Builder.addFields(modelElementDetails: List<ModelElementDetails>) {
        modelElementDetails.forEach {
            field(it.fieldName, it.typeName) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }
        }
    }

    private fun TypeSpec.Builder.addConstructor(modelElementDetails: List<ModelElementDetails>) = constructor {
        addModifiers(Modifier.PUBLIC)

        modelElementDetails.forEach { (typeName, fieldName) ->
            addParameter(typeName, fieldName)
        }
        code {
            modelElementDetails.forEach { (_, fieldName) ->
                assign("this.$fieldName", fieldName)
            }
        }
    }

    private fun TypeSpec.Builder.addGetters(modelElementDetails: List<ModelElementDetails>) {
        modelElementDetails.forEach {
            overrideMethod(it.methodName) {
                returns(it.typeName)

                // Copy all annotations from the interface accessor method to the implementing classes accessor.
                val annotationMirrors = it.enclosedElement.annotationMirrors
                for (annotationMirror in annotationMirrors) {
                    addAnnotation(AnnotationSpec.get(annotationMirror))
                }

                code {
                    `return`(it.fieldName)
                }
            }
        }
    }

    private fun TypeSpec.Builder.addEqualsMethod(outputClassName: ClassName, modelElementDetails: List<ModelElementDetails>) = overrideMethod("equals") {
        returns(TypeName.BOOLEAN)
        addParameter(TypeName.OBJECT, "o")

        code {
            `if`("this == o") {
                `return`("true")
            }
            `if`("o == null || getClass() != o.getClass()") {
                `return`("false")
            }
            newLine()
            createVariable("\$T", "equalsOtherType", "(\$T) o", outputClassName, outputClassName)
            newLine()

            modelElementDetails.forEach { (typeName, fieldName) ->
                if (typeName.isPrimitive) {
                    `if`("$fieldName != equalsOtherType.$fieldName") {
                        `return`("false")
                    }
                } else {
                    if (typeName is ArrayTypeName) {
                        `if`("!java.util.Arrays.equals($fieldName, equalsOtherType.$fieldName)") {
                            `return`("false")
                        }

                    } else {
                        `if`("$fieldName != null ? !$fieldName.equals(equalsOtherType.$fieldName) : equalsOtherType.$fieldName != null") {
                            `return`("false")
                        }
                    }
                }
            }

            newLine()
            `return`("true")
        }
    }

    private fun TypeSpec.Builder.addHashCodeMethod(modelElementDetails: List<ModelElementDetails>) = overrideMethod("hashCode") {
        returns(TypeName.INT)

        code {
            // An optimisation for hash codes which prevents us creating too many temp long variables.
            if (modelElementDetails.any { it.typeName == TypeName.DOUBLE }) {
                addStatement("long temp")
            }

            modelElementDetails.forEachIndexed { index, (typeName, fieldName) ->
                val hashCodeLine: String = if (typeName.isPrimitive) {
                    // The allowed primitive types are: int, long, double, boolean
                    when (typeName) {
                        TypeName.INT -> fieldName
                        TypeName.LONG -> "(int) ($fieldName ^ ($fieldName >>> 32))"
                        TypeName.DOUBLE -> {
                            assign("temp", "java.lang.Double.doubleToLongBits($fieldName)")
                            "(int) (temp ^ (temp >>> 32))"

                        }
                        else -> // Last possible outcome in a boolean.
                            "($fieldName ? 1 : 0)"
                    }
                } else {
                    if (typeName is ArrayTypeName) {
                        "java.util.Arrays.hashCode($fieldName)"

                    } else {
                        "$fieldName != null ? $fieldName.hashCode() : 0"
                    }
                }

                if (index == 0) {
                    createVariable("int", "hashCodeReturnValue", hashCodeLine)
                } else {
                    assign("hashCodeReturnValue", "31 * hashCodeReturnValue + ($hashCodeLine)")
                }
            }

            // If we have no elements, 'hashCodeReturnValue' won't be initialised!
            if (modelElementDetails.isNotEmpty()) {
                `return`("hashCodeReturnValue")
            } else {
                `return`("0")
            }
        }
    }

    private fun TypeSpec.Builder.addToStringMethod(element: TypeElement, modelElementDetails: List<ModelElementDetails>) = overrideMethod("toString") {
        returns(TypeName.get(String::class.java))

        code {
            add("""return "${ClassName.get(element).simpleName()}{" +""")
            newLine()

            modelElementDetails.forEachIndexed { index, (typeName, fieldName) ->
                // Add to the toString method.
                add("\t\t\"")
                if (index > 0) {
                    add(", ")
                }
                if (typeName is ArrayTypeName) {
                    add("""$fieldName=" + java.util.Arrays.toString($fieldName) +""")

                } else {
                    add("""$fieldName=" + $fieldName +""")
                }
                newLine()
            }

            addStatement("\t\t'}'")
        }
    }

    private fun createOutputClassName(element: TypeElement): ClassName {
        return ClassName.get(element).let {
            ClassName.get(it.packageName(), generateClassName(it, "GsonPathModel"))
        }
    }

    private fun getMethodElements(element: TypeElement): List<Element> {
        return typeHandler.getAllMembers(element)
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

    private fun createModelElementDetails(element: TypeElement, methodElements: List<Element>): List<ModelElementDetails> {
        return methodElements.map { enclosedElement ->
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

            ModelElementDetails(typeName, fieldName, enclosedElement, methodName, returnTypeMirror)
        }
    }

    data class ModelElementDetails(
            val typeName: TypeName,
            val fieldName: String,
            val enclosedElement: Element,
            val methodName: String,
            val returnTypeMirror: TypeMirror)

    private class StandardElementInfo(override val underlyingElement: Element) : FieldInfoFactory.ElementInfo {

        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
            return underlyingElement.getAnnotation(annotationClass)
        }

        override val annotationNames: List<String>
            get() {
                return underlyingElement.annotationMirrors.map { it ->
                    it.annotationType.asElement().simpleName.toString()
                }
            }
    }

}
