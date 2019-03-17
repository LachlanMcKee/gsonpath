package gsonpath

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.compiler.generateClassName
import gsonpath.generator.Constants
import gsonpath.generator.HandleResult
import gsonpath.generator.extension.subtype.GsonSubTypeCategory
import gsonpath.generator.extension.subtype.GsonSubTypeFactory
import gsonpath.generator.extension.subtype.GsonSubTypeResult
import gsonpath.generator.writeFile
import gsonpath.model.FieldType
import gsonpath.util.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

object SubTypeAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        val supportedAnnotations = getSupportedAnnotations(annotations)
        val customAnnotations = getCustomAnnotations(annotations)

        // Avoid going any further if no supported annotations are found.
        if (supportedAnnotations.isEmpty() && customAnnotations.isEmpty()) {
            return emptyList()
        }

        return getAnnotatedModelElements(env, customAnnotations)
                .map { (element, autoGsonAdapter) ->
                    logger.printMessage("Generating TypeAdapter ($element)")

                    val typeName = ClassName.get(element)
                    val subTypeMetadata = dependencies.subTypeMetadataFactory.getGsonSubType(
                            autoGsonAdapter,
                            GsonSubTypeCategory.SingleValue(FieldType.Other(
                                    typeName = typeName,
                                    elementTypeMirror = element.asType()
                            )),
                            "Type",
                            element)

                    GsonSubTypeFactory.createSubTypeMetadata(typeName, subTypeMetadata)
                            .let { result ->
                                val adapterClassName = ClassName.get(typeName.packageName(),
                                        generateClassName(typeName, "GsonTypeAdapter"))

                                createSubTypeAdapterSpec(adapterClassName, typeName, result)
                                        .writeFile(dependencies.fileWriter, adapterClassName.packageName())
                                HandleResult(arrayOf(typeName), adapterClassName)
                            }
                }
    }

    private fun createSubTypeAdapterSpec(
            adapterClassName: ClassName,
            typeName: ClassName,
            result: GsonSubTypeResult) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        superclass(ParameterizedTypeName.get(ClassName.get(TypeAdapter::class.java), typeName))
        addAnnotation(Constants.GENERATED_ANNOTATION)

        field("mGson", Gson::class.java) {
            addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        }

        result.fieldSpecs.forEach { addField(it) }

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            code {
                assign("this.mGson", "gson")
            }
            addCode(result.constructorCodeBlock)
        }

        result.typeSpecs.forEach { addType(it) }
        addMethod(result.readMethodSpecs)
        addMethod(result.writeMethodSpecs)
    }

    private fun getSupportedAnnotations(annotations: Set<TypeElement>) =
            annotations
                    .asSequence()
                    .map(ClassName::get)
                    .filter { annotationClassName ->
                        annotationClassName == ClassName.get(GsonSubtype::class.java)
                    }
                    .toList()

    private fun getCustomAnnotations(annotations: Set<TypeElement>) =
            annotations.filter { it.getAnnotation(GsonSubtype::class.java) != null }

    private fun getAnnotatedModelElements(env: RoundEnvironment,
                                          customAnnotations: List<TypeElement>): Set<ElementAndAutoGson> {
        return env
                .getElementsAnnotatedWith(GsonSubtype::class.java)
                .asSequence()
                .filter { it.kind == ElementKind.CLASS }
                .map {
                    ElementAndAutoGson(it as TypeElement, it.getAnnotation(GsonSubtype::class.java))
                }
                .filter {
                    !customAnnotations.contains(it.element)
                }
                .plus(
                        customAnnotations.flatMap { customAnnotation ->
                            env
                                    .getElementsAnnotatedWith(customAnnotation)
                                    .filter { it.kind == ElementKind.CLASS }
                                    .map {
                                        ElementAndAutoGson(it as TypeElement, customAnnotation.getAnnotation(GsonSubtype::class.java))
                                    }
                        }
                )
                .toSet()
    }

    private data class ElementAndAutoGson(
            val element: TypeElement,
            val autoGsonAdapter: GsonSubtype
    )
}