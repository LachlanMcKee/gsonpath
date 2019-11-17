package gsonpath.adapter.subType

import com.google.gson.Gson
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import gsonpath.GsonPathTypeAdapter
import gsonpath.GsonSubtype
import gsonpath.adapter.AdapterFactory
import gsonpath.adapter.AdapterMetadata
import gsonpath.adapter.common.GsonSubTypeFactory
import gsonpath.adapter.common.GsonSubTypeResult
import gsonpath.adapter.util.ElementAndAnnotation
import gsonpath.adapter.util.writeFile
import gsonpath.compiler.generateClassName
import gsonpath.dependencies.Dependencies
import gsonpath.model.FieldType
import gsonpath.util.GeneratedAdapterUtil.createGeneratedAdapterAnnotation
import gsonpath.util.TypeSpecExt
import gsonpath.util.constructor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

object SubTypeAdapterFactory : AdapterFactory<GsonSubtype>() {

    override fun getHandledElement(
            element: TypeElement,
            elementClassName: ClassName,
            adapterClassName: ClassName): AdapterMetadata {

        return AdapterMetadata(element, listOf(elementClassName), adapterClassName)
    }

    override fun getAnnotationClass() = GsonSubtype::class.java

    override fun getSupportedElementKinds() = listOf(ElementKind.CLASS)

    override fun generate(
            env: RoundEnvironment,
            dependencies: Dependencies,
            elementAndAnnotation: ElementAndAnnotation<GsonSubtype>) {

        generateAdapter(elementAndAnnotation.element, elementAndAnnotation.annotation, dependencies)
    }

    private fun generateAdapter(
            element: TypeElement,
            gsonSubtype: GsonSubtype,
            dependencies: Dependencies) {

        val typeName = ClassName.get(element)
        val subTypeMetadata = dependencies.subTypeMetadataFactory.getGsonSubType(
                gsonSubtype,
                FieldType.Other(
                        typeName = typeName,
                        elementTypeMirror = element.asType()
                ),
                "Type",
                element)

        GsonSubTypeFactory.createSubTypeMetadata(typeName, subTypeMetadata)
                .also { result ->
                    val adapterClassName = ClassName.get(typeName.packageName(),
                            generateClassName(typeName, "GsonTypeAdapter"))

                    createSubTypeAdapterSpec(adapterClassName, typeName, result)
                            .writeFile(dependencies.fileWriter, adapterClassName.packageName())
                }
    }

    private fun createSubTypeAdapterSpec(
            adapterClassName: ClassName,
            typeName: ClassName,
            result: GsonSubTypeResult) = TypeSpecExt.finalClassBuilder(adapterClassName).apply {

        superclass(ParameterizedTypeName.get(ClassName.get(GsonPathTypeAdapter::class.java), typeName))
        addAnnotation(createGeneratedAdapterAnnotation(typeName))

        // Add the constructor which takes a gson instance for future use.
        constructor {
            addModifiers(Modifier.PUBLIC)
            addParameter(Gson::class.java, "gson")
            addStatement("super(gson)")
        }

        addMethod(result.readMethodSpecs)
        addMethod(result.writeMethodSpecs)
    }
}