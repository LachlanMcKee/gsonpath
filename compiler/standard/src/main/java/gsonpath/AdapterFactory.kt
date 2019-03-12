package gsonpath

import gsonpath.generator.HandleResult
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

interface AdapterFactory {
    fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<HandleResult>
}