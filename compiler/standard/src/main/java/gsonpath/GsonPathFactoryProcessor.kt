package gsonpath

import gsonpath.adapter.AdapterMetadata
import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.Dependencies
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class GsonPathFactoryProcessor : CommonProcessor() {

    override fun handleAnnotations(annotations: Set<TypeElement>, env: RoundEnvironment, dependencies: Dependencies) {
        val typeAdapterElements =
                StandardAdapterFactory.getHandledElements(env, annotations)
                        .plus(SubTypeAdapterFactory.getHandledElements(env, annotations))
                        .plus(EnumAdapterFactory.getHandledElements(env, annotations))

        if (typeAdapterElements.isNotEmpty()) {
            generateFactories(env, dependencies, typeAdapterElements)
        }
    }

    private fun generateFactories(
            env: RoundEnvironment,
            dependencies: Dependencies,
            typeAdapterElements: List<AdapterMetadata>) {

        val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

        when {
            gsonPathFactories.count() == 0 -> {
                throw ProcessingException("An interface annotated with @AutoGsonAdapterFactory " +
                        "(that directly extends com.google.gson.TypeAdapterFactory) must exist before the " +
                        "annotation processor can succeed. See the AutoGsonAdapterFactory annotation " +
                        "for further details.")
            }
            gsonPathFactories.count() > 1 -> {
                throw ProcessingException("Only one interface annotated with @AutoGsonAdapterFactory can exist")
            }
            else -> {
                dependencies
                        .typeAdapterFactoryGenerator
                        .generate(gsonPathFactories.first() as TypeElement, typeAdapterElements)
            }
        }
    }

    override fun processorDescription() = "adapter factories"
}