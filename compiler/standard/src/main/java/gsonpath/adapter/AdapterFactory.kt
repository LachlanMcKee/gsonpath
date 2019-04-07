package gsonpath.adapter

import com.squareup.javapoet.ClassName
import gsonpath.dependencies.Dependencies
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

interface AdapterFactory {
    fun generateGsonAdapters(
            env: RoundEnvironment,
            logger: Logger,
            annotations: Set<TypeElement>,
            dependencies: Dependencies): List<AdapterGenerationResult>
}

class AdapterGenerationResult(
        val adapterGenericTypeClassNames: Array<ClassName>,
        val adapterClassName: ClassName)