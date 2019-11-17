package gsonpath

import gsonpath.adapter.enums.EnumAdapterFactory
import gsonpath.adapter.standard.StandardAdapterFactory
import gsonpath.adapter.subType.SubTypeAdapterFactory
import gsonpath.dependencies.DependencyFactory
import gsonpath.util.Logger
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class GsonPathFactoryProcessor : CommonProcessor() {

    override fun handleAnnotations(annotations: Set<TypeElement>, env: RoundEnvironment, logger: Logger) {
        val dependencies = DependencyFactory.create(processingEnv)

        val typeAdapterElements =
                StandardAdapterFactory.getHandledElements(env, annotations)
                        .plus(SubTypeAdapterFactory.getHandledElements(env, annotations))
                        .plus(EnumAdapterFactory.getHandledElements(env, annotations))

        if (typeAdapterElements.isNotEmpty()) {
            val gsonPathFactories = env.getElementsAnnotatedWith(AutoGsonAdapterFactory::class.java)

            when {
                gsonPathFactories.count() == 0 -> {
                    throw ProcessingException("An interface annotated with @AutoGsonAdapterFactory (that directly extends " +
                            "com.google.gson.TypeAdapterFactory) must exist before the annotation processor can succeed. " +
                            "See the AutoGsonAdapterFactory annotation for further details.")
                }
                gsonPathFactories.count() > 1 -> {
                    throw ProcessingException("Only one interface annotated with @AutoGsonAdapterFactory can exist")
                }
                else -> {
                    val factoryElement = gsonPathFactories.first()
                    dependencies.typeAdapterFactoryGenerator.generate(factoryElement as TypeElement, typeAdapterElements)
                }
            }
        }
    }

    override fun processorDescription() = "adapter factories"
}