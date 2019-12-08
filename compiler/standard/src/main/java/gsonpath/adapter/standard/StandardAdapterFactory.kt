package gsonpath.adapter.standard

import gsonpath.AutoGsonAdapter
import gsonpath.LazyFactoryMetadata
import gsonpath.adapter.AdapterFactory
import gsonpath.adapter.AdapterGenerationResult
import gsonpath.adapter.util.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.dependencies.Dependencies
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object StandardAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            lazyFactoryMetadata: LazyFactoryMetadata,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<AdapterGenerationResult> {

        return getAnnotatedModelElements<AutoGsonAdapter>(env, annotations, listOf(ElementKind.CLASS, ElementKind.INTERFACE))
                .onEach { logger.printMessage("Generating TypeAdapter (${it.element})") }
                .map {
                    dependencies.standardGsonAdapterGenerator.handle(it.element, it.annotation, lazyFactoryMetadata)
                }
    }

}