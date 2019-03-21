package gsonpath

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.AdapterFactoryUtil.getAnnotatedModelElements
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
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

object SubTypeAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        return getAnnotatedModelElements<GsonSubtype>(env, annotations)
                .map { generateAdapter(it.element, it.annotation, logger, dependencies) }
    }

    private fun generateAdapter(
            element: TypeElement,
            gsonSubtype: GsonSubtype,
            logger: Logger,
            dependencies: Dependencies): HandleResult {

        logger.printMessage("Generating TypeAdapter ($element)")

        val typeName = ClassName.get(element)
        val subTypeMetadata = dependencies.subTypeMetadataFactory.getGsonSubType(
                gsonSubtype,
                GsonSubTypeCategory.SingleValue(FieldType.Other(
                        typeName = typeName,
                        elementTypeMirror = element.asType()
                )),
                "Type",
                element)

        return GsonSubTypeFactory.createSubTypeMetadata(typeName, subTypeMetadata)
                .let { result ->
                    val adapterClassName = ClassName.get(typeName.packageName(),
                            generateClassName(typeName, "GsonTypeAdapter"))

                    createSubTypeAdapterSpec(adapterClassName, typeName, result)
                            .writeFile(dependencies.fileWriter, adapterClassName.packageName())
                    HandleResult(arrayOf(typeName), adapterClassName)
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
}