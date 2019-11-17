package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class GsonPathAdapterProcessor : CommonProcessor() {

    override fun handleAnnotations(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        val dependencies = DependencyFactory.create(processingEnv)
        StandardAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
        SubTypeAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
        EnumAdapterFactory.generateGsonAdapters(env, logger, annotations, dependencies)
    }

    override fun processorDescription() = "adapters"
}