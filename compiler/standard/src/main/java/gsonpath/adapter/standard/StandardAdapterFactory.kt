package gsonpath.adapter.standard

import gsonpath.AutoGsonAdapter
import gsonpath.adapter.AdapterFactory
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
            annotations: Set<TypeElement>,
            dependencies: Dependencies) {

        return getAnnotatedModelElements<AutoGsonAdapter>(env, annotations, listOf(ElementKind.CLASS, ElementKind.INTERFACE))
                .forEach {
                    logger.printMessage("Generating TypeAdapter (${it.element})")
                    dependencies.standardGsonAdapterGenerator.handle(it.element, it.annotation)
                }
    }

}