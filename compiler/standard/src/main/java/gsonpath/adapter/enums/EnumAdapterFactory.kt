package gsonpath.adapter.enums

import com.squareup.javapoet.ClassName
import gsonpath.AutoGsonAdapter
import gsonpath.adapter.AdapterFactory
import gsonpath.adapter.AdapterMetadata
import gsonpath.adapter.util.ElementAndAnnotation
import gsonpath.compiler.generateClassName
import gsonpath.dependencies.Dependencies
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object EnumAdapterFactory : AdapterFactory<AutoGsonAdapter>() {
    override fun getHandledElements(
            env: RoundEnvironment,
            annotations: Set<TypeElement>): List<AdapterMetadata> {

        return getAutoGsonAdapterElements(env, annotations)
                .map {
                    val typeName = ClassName.get(it.element)
                    val adapterClassName = ClassName.get(typeName.packageName(),
                            generateClassName(typeName, "GsonTypeAdapter"))
                    AdapterMetadata(
                            element = it.element,
                            elementClassNames = listOf(typeName),
                            typeAdapterClassName = adapterClassName
                    )
                }
    }

    override fun getAnnotationClass() = AutoGsonAdapter::class.java

    override fun getSupportedElementKinds() = listOf(ElementKind.ENUM)

    override fun generate(
            env: RoundEnvironment,
            dependencies: Dependencies,
            elementAndAnnotation: ElementAndAnnotation<AutoGsonAdapter>) {

        dependencies.enumGsonAdapterGenerator.handle(elementAndAnnotation.element, elementAndAnnotation.annotation)
    }
}