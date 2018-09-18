package gsonpath.generator.interf

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.compiler.generateClassName
import gsonpath.generator.writeFile
import gsonpath.model.FieldInfoFactory
import gsonpath.model.FieldInfoFactory.InterfaceFieldInfo
import gsonpath.model.FieldInfoFactory.InterfaceInfo
import gsonpath.util.*
import java.util.*
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
        val modelClassName = ClassName.get(element)
        val outputClassName: ClassName = createOutputClassName(modelClassName)

        val generatedJavaPoetAnnotation = AnnotationSpec.builder(Generated::class.java)
                .addMember("value", "\"gsonpath.GsonProcessor\"")
                .addMember("comments", "\"https://github.com/LachlanMcKee/gsonpath\"")
                .build()

        val typeBuilder = TypeSpecExt.finalClassBuilder(outputClassName)
                .addSuperinterface(modelClassName)
                .addAnnotation(generatedJavaPoetAnnotation)

        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)

        val methodElements = getMethodElements(element)

        // Equals method
        val equalsCodeBlock = CodeBlock.builder()
                .addStatement("if (this == o) return true")
                .addStatement("if (o == null || getClass() != o.getClass()) return false")
                .newLine()
                .addStatement("\$T equalsOtherType = (\$T) o", outputClassName, outputClassName)
                .newLine()

        // Hash code method
        val hasCodeCodeBlock = CodeBlock.builder()

        // ToString method
        val toStringCodeBlock = CodeBlock.builder()
                .add("""return "${modelClassName.simpleName()}{" +""")
                .newLine()

        // An optimisation for hash codes which prevents us creating too many temp long variables.
        val hasDoubleField = methodElements
                .map { it.asType() as ExecutableType }
                .map { it.returnType }
                .any { TypeName.get(it) == TypeName.DOUBLE }

        if (hasDoubleField) {
            hasCodeCodeBlock.addStatement("long temp")
        }

        val interfaceInfoList = ArrayList<InterfaceFieldInfo>()
        for (elementIndex in methodElements.indices) {
            val enclosedElement = methodElements[elementIndex]

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

            typeBuilder.addField(typeName, fieldName, Modifier.PRIVATE, Modifier.FINAL)

            // Accessor method
            typeBuilder.overrideMethod(methodName) {
                returns(typeName)
                addStatement("return $fieldName")

                // Copy all annotations from the interface accessor method to the implementing classes accessor.
                val annotationMirrors = enclosedElement.annotationMirrors
                for (annotationMirror in annotationMirrors) {
                    addAnnotation(AnnotationSpec.get(annotationMirror))
                }
            }

            // Add the parameter to the constructor
            constructorBuilder.addParameter(typeName, fieldName)
                    .addStatement("this.$fieldName = $fieldName")

            interfaceInfoList.add(InterfaceFieldInfo(StandardElementInfo(enclosedElement), typeName, returnTypeMirror, fieldName, methodName))

            // Add to the equals method
            if (typeName.isPrimitive) {
                equalsCodeBlock.addStatement("if ($fieldName != equalsOtherType.$fieldName) return false")
            } else {
                if (typeName is ArrayTypeName) {
                    equalsCodeBlock.addStatement("if (!java.util.Arrays.equals($fieldName, equalsOtherType.$fieldName)) return false")

                } else {
                    equalsCodeBlock.addStatement("if ($fieldName != null ? !$fieldName.equals(equalsOtherType.$fieldName) : equalsOtherType.$fieldName != null) return false")
                }
            }

            // Add to the hash code method
            val hashCodeLine: String = if (typeName.isPrimitive) {
                // The allowed primitive types are: int, long, double, boolean
                when (typeName) {
                    TypeName.INT -> fieldName
                    TypeName.LONG -> "(int) ($fieldName ^ ($fieldName >>> 32))"
                    TypeName.DOUBLE -> {
                        hasCodeCodeBlock.addStatement("temp = java.lang.Double.doubleToLongBits($fieldName)")
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

            if (elementIndex == 0) {
                hasCodeCodeBlock.addStatement("int hashCodeReturnValue = $hashCodeLine")
            } else {
                hasCodeCodeBlock.addStatement("hashCodeReturnValue = 31 * hashCodeReturnValue + ($hashCodeLine)")
            }

            // Add to the toString method.
            toStringCodeBlock.add("\t\t\"")
            if (elementIndex > 0) {
                toStringCodeBlock.add(", ")
            }
            if (typeName is ArrayTypeName) {
                toStringCodeBlock.add("""$fieldName=" + java.util.Arrays.toString($fieldName) +""")

            } else {
                toStringCodeBlock.add("""$fieldName=" + $fieldName +""")
            }
            toStringCodeBlock.newLine()
        }

        typeBuilder.addMethod(constructorBuilder.build())

        // Add the equals method
        typeBuilder.overrideMethod("equals") {
            returns(TypeName.BOOLEAN)
            addParameter(TypeName.OBJECT, "o")
            addCode(equalsCodeBlock.newLine()
                    .addStatement("return true")
                    .build())
        }

        // If we have no elements, 'hashCodeReturnValue' won't be initialised!
        if (methodElements.isNotEmpty()) {
            hasCodeCodeBlock.addStatement("return hashCodeReturnValue")
        } else {
            hasCodeCodeBlock.addStatement("return 0")
        }

        // Add the hashCode method
        typeBuilder.overrideMethod("hashCode") {
            returns(TypeName.INT)
            addCode(hasCodeCodeBlock.build())
        }

        // Add the toString method
        typeBuilder.overrideMethod("toString") {
            returns(TypeName.get(String::class.java))
            addCode(toStringCodeBlock.build())
            addStatement("\t\t'}'", modelClassName.simpleName())
        }

        if (!typeBuilder.writeFile(fileWriter, logger, outputClassName.packageName())) {
            throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
        }

        return InterfaceInfo(outputClassName, interfaceInfoList)
    }

    private fun createOutputClassName(modelClassName: ClassName): ClassName {
        return ClassName.get(modelClassName.packageName(), generateClassName(modelClassName, "GsonPathModel"))
    }

    private fun getMethodElements(element: TypeElement): List<Element> {
        return typeHandler.getAllMembers(element)
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
    }

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
