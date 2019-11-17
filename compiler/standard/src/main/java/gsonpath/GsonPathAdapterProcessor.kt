package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.Dependencies
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class GsonPathAdapterProcessor : CommonProcessor() {

    override fun handleAnnotations(annotations: Set<TypeElement>, env: RoundEnvironment, dependencies: Dependencies) {
        StandardAdapterFactory.generateGsonAdapters(env, annotations, dependencies)
        SubTypeAdapterFactory.generateGsonAdapters(env, annotations, dependencies)
        EnumAdapterFactory.generateGsonAdapters(env, annotations, dependencies)
    }

    override fun processorDescription() = "adapters"
}