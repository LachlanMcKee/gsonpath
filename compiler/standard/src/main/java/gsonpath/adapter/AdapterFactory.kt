package gsonpath.adapter

import com.squareup.javapoet.ClassName
import gsonpath.adapter.util.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.adapter.util.ElementAndAnnotation
import gsonpath.compiler.generateClassName
import gsonpath.dependencies.Dependencies
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

abstract class AdapterFactory<T : Annotation> {

    fun generateGsonAdapters(
            env: RoundEnvironment,
            annotations: Set<TypeElement>,
            dependencies: Dependencies) {

        return getAutoGsonAdapterElements(env, annotations)
                .forEach {
                    dependencies.logger.printMessage("Generating TypeAdapter (${it.element})")
                    generate(env, dependencies, it)
                }
    }

    fun getHandledElements(env: RoundEnvironment, annotations: Set<TypeElement>): List<AdapterMetadata> {
        return getAutoGsonAdapterElements(env, annotations)
                .map { (element, _) ->
                    val typeName = ClassName.get(element)
                    val adapterClassName = ClassName.get(typeName.packageName(),
                            generateClassName(typeName, "GsonTypeAdapter"))

                    getHandledElement(element, typeName, adapterClassName)
                }
    }

    private fun getAutoGsonAdapterElements(
            env: RoundEnvironment,
            annotations: Set<TypeElement>): Set<ElementAndAnnotation<T>> {

        return getAnnotatedModelElements(getAnnotationClass(), env, annotations, getSupportedElementKinds())
    }

    abstract fun getHandledElement(
            element: TypeElement,
            elementClassName: ClassName,
            adapterClassName: ClassName): AdapterMetadata

    protected abstract fun getSupportedElementKinds(): List<ElementKind>

    protected abstract fun getAnnotationClass(): Class<T>

    protected abstract fun generate(
            env: RoundEnvironment,
            dependencies: Dependencies,
            elementAndAnnotation: ElementAndAnnotation<T>)
}

data class AdapterMetadata(
        val element: TypeElement,
        val elementClassNames: List<ClassName>,
        val typeAdapterClassName: ClassName
)
