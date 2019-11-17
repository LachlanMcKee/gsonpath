package gsonpath.adapter

import com.squareup.javapoet.ClassName
import gsonpath.adapter.util.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.adapter.util.ElementAndAnnotation
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

    protected fun getAutoGsonAdapterElements(
            env: RoundEnvironment,
            annotations: Set<TypeElement>): Set<ElementAndAnnotation<T>> {

        return getAnnotatedModelElements(getAnnotationClass(), env, annotations, getSupportedElementKinds())
    }

    abstract fun getHandledElements(env: RoundEnvironment, annotations: Set<TypeElement>): List<AdapterMetadata>

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
