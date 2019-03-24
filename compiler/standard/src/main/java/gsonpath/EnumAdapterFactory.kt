package gsonpath

import gsonpath.AdapterFactoryUtil.getAnnotatedModelElements
import gsonpath.generator.HandleResult
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object EnumAdapterFactory : AdapterFactory {

    override fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult> {

        return getAnnotatedModelElements<AutoGsonAdapter>(env, annotations, listOf(ElementKind.ENUM))
                .onEach { logger.printMessage("Generating TypeAdapter (${it.element})") }
                .map { dependencies.enumGsonAdapterGenerator.handle(it.element, it.annotation) }
    }
}