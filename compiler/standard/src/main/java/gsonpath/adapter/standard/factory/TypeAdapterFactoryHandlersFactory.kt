package gsonpath.adapter.standard.factory

import com.google.gson.TypeAdapterFactory
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterMetadata
import javax.lang.model.element.TypeElement

object TypeAdapterFactoryHandlersFactory {
    private val typeAdapterFactoryTypeName = TypeName.get(TypeAdapterFactory::class.java)

    fun createResults(
            factoryElement: TypeElement,
            generatedGsonAdapters: List<AdapterMetadata>): Map<String, List<AdapterMetadata>> {

        if (generatedGsonAdapters.isEmpty()) {
            return emptyMap()
        }

        // Only interfaces are accepted (for simplicity)
        if (!factoryElement.kind.isInterface) {
            throw ProcessingException("Types annotated with @AutoGsonAdapterFactory must be an interface " +
                    "that directly extends com.google.gson.TypeAdapterFactory.", factoryElement)
        }

        // Ensure that the factory element only extends TypeAdapterFactory
        val factoryInterfaces = factoryElement.interfaces
        if (factoryInterfaces.size != 1 || TypeName.get(factoryInterfaces[0]) != typeAdapterFactoryTypeName) {
            throw ProcessingException("Interfaces annotated with @AutoGsonAdapterFactory must extend " +
                    "com.google.gson.TypeAdapterFactory and no other interfaces.", factoryElement)
        }

        return generatedGsonAdapters.fold(emptyMap()) { map, generatedGsonAdapter ->
            val packageName = ClassName.get(generatedGsonAdapter.element).packageName()

            val newList: List<AdapterMetadata> =
                    map[packageName]?.plus(generatedGsonAdapter) ?: listOf(generatedGsonAdapter)

            return@fold map.plus(Pair(packageName, newList))
        }
    }
}