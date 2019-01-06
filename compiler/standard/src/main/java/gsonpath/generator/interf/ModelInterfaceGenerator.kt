package gsonpath.generator.interf

import com.squareup.javapoet.*
import gsonpath.ProcessingException
import gsonpath.compiler.generateClassName
import gsonpath.generator.Constants.GENERATED_ANNOTATION
import gsonpath.generator.Constants.NULL
import gsonpath.generator.writeFile
import gsonpath.model.FieldInfoFactory
import gsonpath.model.FieldInfoFactory.InterfaceFieldInfo
import gsonpath.model.FieldInfoFactory.InterfaceInfo
import gsonpath.util.*
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
        addAnnotation(GENERATED_ANNOTATION)

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
                `return`(TRUE)
            }
            `if`("o == null || getClass() != o.getClass()") {
                `return`(FALSE)
            }
            newLine()
            createVariable(outputClassName, EQUALS_OTHER_TYPE, "(\$T) o", outputClassName)
            newLine()

            modelMetadataList.forEach { (typeName, fieldName) ->
                if (typeName.isPrimitive) {
                    `if`("$fieldName != $EQUALS_OTHER_TYPE.$fieldName") {
                        `return`(FALSE)
                    }
                } else {
                    if (typeName is ArrayTypeName) {
                        `if`("!java.util.Arrays.equals($fieldName, $EQUALS_OTHER_TYPE.$fieldName)") {
                            `return`(FALSE)
                        }

                    } else {
                        `if`("$fieldName != $NULL ? !$fieldName.equals($EQUALS_OTHER_TYPE.$fieldName) : $EQUALS_OTHER_TYPE.$fieldName != $NULL") {
                            `return`(FALSE)
                        }
                    }
                }
            }

            newLine()
            `return`(TRUE)
        }
    }

    private fun TypeSpec.Builder.addHashCodeMethod(modelMetadataList: List<InterfaceModelMetadata>) = overrideMethod("hashCode") {
        returns(TypeName.INT)

        code {
            // An optimisation for hash codes which prevents us creating too many temp long variables.
            if (modelMetadataList.any { it.typeName == TypeName.DOUBLE }) {
                addStatement("long $TEMP")
            }

            modelMetadataList.forEachIndexed { index, (typeName, fieldName) ->
                val hashCodeLine: String = if (typeName.isPrimitive) {
                    // The allowed primitive types are: int, long, double, boolean
                    when (typeName) {
                        TypeName.INT -> fieldName
                        TypeName.LONG -> "(int) ($fieldName ^ ($fieldName >>> 32))"
                        TypeName.DOUBLE -> {
                            assign(TEMP, "java.lang.Double.doubleToLongBits($fieldName)")
                            "(int) ($TEMP ^ ($TEMP >>> 32))"
                        }
                        else -> // Last possible outcome in a boolean.
                            "($fieldName ? 1 : 0)"
                    }
                } else {
                    if (typeName is ArrayTypeName) {
                        "java.util.Arrays.hashCode($fieldName)"

                    } else {
                        "$fieldName != $NULL ? $fieldName.hashCode() : 0"
                    }
                }

                if (index == 0) {
                    createVariable("int", HASH_CODE_RETURN_VALUE, hashCodeLine)
                } else {
                    assign(HASH_CODE_RETURN_VALUE, "31 * $HASH_CODE_RETURN_VALUE + ($hashCodeLine)")
                }
            }

            // If we have no elements, 'hashCodeReturnValue' won't be initialised!
            if (modelMetadataList.isNotEmpty()) {
                `return`(HASH_CODE_RETURN_VALUE)
            } else {
                `return`("0")
            }
        }
    }

    private fun TypeSpec.Builder.addToStringMethod(element: TypeElement, modelMetadataList: List<InterfaceModelMetadata>) = overrideMethod("toString") {
        returns(TypeName.get(String::class.java))

        val className = ClassName.get(element).simpleName()
        code {
            add("""return "$className{" +""")
            newLine()
            indent()
            add("\"")

            modelMetadataList.forEachIndexed { index, (typeName, fieldName) ->
                // Add to the toString method.
                if (index > 0) {
                    add("\", ")
                }
                if (typeName is ArrayTypeName) {
                    add("""$fieldName=" + java.util.Arrays.toString($fieldName) +""")

                } else {
                    add("""$fieldName=" + $fieldName +""")
                }
                newLine()
            }

            addStatement("'}'")
        }
    }

    private fun createOutputClassName(element: TypeElement): ClassName {
        return ClassName.get(element).let {
            ClassName.get(it.packageName(), generateClassName(it, "GsonPathModel"))
        }
    }

    private class StandardElementInfo(override val underlyingElement: Element) : FieldInfoFactory.ElementInfo {

        override fun <T : Annotation> getAnnotation(annotationClass: Class<T>): T? {
            return underlyingElement.getAnnotationEx(annotationClass)
        }

        override val annotationNames: List<String>
            get() {
                return underlyingElement.annotationMirrors.map { it ->
                    it.annotationType.asElement().simpleName.toString()
                }
            }
    }

    private companion object {
        private const val TRUE = "true"
        private const val FALSE = "false"
        private const val TEMP = "temp"
        private const val HASH_CODE_RETURN_VALUE = "hashCodeReturnValue"
        private const val EQUALS_OTHER_TYPE = "equalsOtherType"
    }
}
