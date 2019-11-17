package gsonpath.adapter.enums

import com.squareup.javapoet.ClassName
import gsonpath.AutoGsonAdapter
import gsonpath.adapter.AdapterFactory
import gsonpath.adapter.AdapterMetadata
import gsonpath.adapter.util.ElementAndAnnotation
import gsonpath.dependencies.Dependencies
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object EnumAdapterFactory : AdapterFactory<AutoGsonAdapter>() {
    override fun getHandledElement(
            element: TypeElement,
            elementClassName: ClassName,
            adapterClassName: ClassName): AdapterMetadata {

        return AdapterMetadata(element, listOf(elementClassName), adapterClassName)
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