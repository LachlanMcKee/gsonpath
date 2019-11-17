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
    override fun getHandledElement(
            element: TypeElement,
            elementClassName: ClassName,
            adapterClassName: ClassName): AdapterMetadata {

        val elementClassNames = if (element.kind.isInterface) {
            listOf(
                    ClassName.get(elementClassName.packageName(), generateClassName(elementClassName, "GsonPathModel")),
                    elementClassName
            )
        } else {
            listOf(elementClassName)
        }

        return AdapterMetadata(element, elementClassNames, adapterClassName)
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