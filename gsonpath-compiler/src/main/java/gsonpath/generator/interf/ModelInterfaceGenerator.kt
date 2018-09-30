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
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ModelInterfaceGenerator(
        private val interfaceModelMetadataFactory: InterfaceModelMetadataFactory,
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

        val modelMetadataList = interfaceModelMetadataFactory.createMetadata(element)

        addFields(modelMetadataList)
        addConstructor(modelMetadataList)
        addGetters(modelMetadataList)
        addEqualsMethod(outputClassName, modelMetadataList)
        addHashCodeMethod(modelMetadataList)
        addToStringMethod(element, modelMetadataList)

        if (!writeFile(fileWriter, logger, outputClassName.packageName())) {
            throw ProcessingException("Failed to write generated file: " + outputClassName.simpleName())
        }

        return InterfaceInfo(outputClassName, modelMetadataList.map {
            InterfaceFieldInfo(StandardElementInfo(it.enclosedElement),
                    it.typeName, it.returnTypeMirror, it.fieldName, it.methodName)
        })
    }

    private fun TypeSpec.Builder.addFields(modelMetadataList: List<InterfaceModelMetadata>) {
        modelMetadataList.forEach {
            field(it.fieldName, it.typeName) {
                addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            }
        }
    }

    private fun TypeSpec.Builder.addConstructor(modelMetadataList: List<InterfaceModelMetadata>) = constructor {
        addModifiers(Modifier.PUBLIC)

        modelMetadataList.forEach { (typeName, fieldName) ->
            addParameter(typeName, fieldName)
        }
        code {
            modelMetadataList.forEach { (_, fieldName) ->
                assign("this.$fieldName", fieldName)
            }
        }
    }

    private fun TypeSpec.Builder.addGetters(modelMetadataList: List<InterfaceModelMetadata>) {
        modelMetadataList.forEach {
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

    private fun TypeSpec.Builder.addEqualsMethod(outputClassName: ClassName, modelMetadataList: List<InterfaceModelMetadata>) = overrideMethod("equals") {
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

            modelMetadataList.forEach { (typeName, fieldName) ->
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

    private fun TypeSpec.Builder.addHashCodeMethod(modelMetadataList: List<InterfaceModelMetadata>) = overrideMethod("hashCode") {
        returns(TypeName.INT)

        code {
            // An optimisation for hash codes which prevents us creating too many temp long variables.
            if (modelMetadataList.any { it.typeName == TypeName.DOUBLE }) {
                addStatement("long temp")
            }

            modelMetadataList.forEachIndexed { index, (typeName, fieldName) ->
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
            if (modelMetadataList.isNotEmpty()) {
                `return`("hashCodeReturnValue")
            } else {
                `return`("0")
            }
        }
    }

    private fun TypeSpec.Builder.addToStringMethod(element: TypeElement, modelMetadataList: List<InterfaceModelMetadata>) = overrideMethod("toString") {
        returns(TypeName.get(String::class.java))

        code {
            add("""return "${ClassName.get(element).simpleName()}{" +""")
            newLine()

            modelMetadataList.forEachIndexed { index, (typeName, fieldName) ->
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
