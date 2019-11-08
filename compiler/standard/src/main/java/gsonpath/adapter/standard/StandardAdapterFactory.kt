package gsonpath.adapter.standard

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

object StandardAdapterFactory : AdapterFactory<AutoGsonAdapter>() {
    override fun getHandledElements(
            env: RoundEnvironment,
            annotations: Set<TypeElement>): List<AdapterMetadata> {

        return getAutoGsonAdapterElements(env, annotations)
                .map {
                    val typeName = ClassName.get(it.element)
                    val adapterClassName = ClassName.get(typeName.packageName(),
                            generateClassName(typeName, "GsonTypeAdapter"))

                    val elementClassNames = if (it.element.kind.isInterface) {
                        listOf(typeName, ClassName.get(typeName.packageName(), generateClassName(typeName, "GsonPathModel")))
                    } else {
                        listOf(typeName)
                    }

                    AdapterMetadata(
                            element = it.element,
                            elementClassNames = elementClassNames,
                            typeAdapterClassName = adapterClassName
                    )
                }
    }

    override fun getAnnotationClass() = AutoGsonAdapter::class.java

    override fun getSupportedElementKinds() = listOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun generate(
            env: RoundEnvironment,
            dependencies: Dependencies,
            elementAndAnnotation: ElementAndAnnotation<AutoGsonAdapter>) {

        dependencies.standardGsonAdapterGenerator.handle(elementAndAnnotation.element, elementAndAnnotation.annotation)
    }
}