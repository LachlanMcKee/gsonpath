package gsonpath.adapter.standard.factory

import com.google.gson.TypeAdapterFactory
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.adapter.AdapterGenerationResult
import javax.lang.model.element.TypeElement

object TypeAdapterFactoryHandlersFactory {
    private val typeAdapterFactoryTypeName = TypeName.get(TypeAdapterFactory::class.java)

    fun createResults(
            factoryElement: TypeElement,
            generatedGsonAdapters: List<AdapterGenerationResult>): Map<String, List<AdapterGenerationResult>> {

        if (generatedGsonAdapters.isEmpty()) {
            return emptyMap()
        }

        // Ensure that the factory element only extends TypeAdapterFactory
        val factoryInterfaces = factoryElement.interfaces
        if (factoryInterfaces.size != 1 || TypeName.get(factoryInterfaces[0]) != typeAdapterFactoryTypeName) {
            throw ProcessingException("Interfaces annotated with @AutoGsonAdapterFactory must extend " +
                    "com.google.gson.TypeAdapterFactory and no other interfaces.", factoryElement)
        }

        return generatedGsonAdapters.fold(emptyMap()) { map, generatedGsonAdapter ->
            val packageName = generatedGsonAdapter.adapterClassName.packageName()

            val newList: List<AdapterGenerationResult> =
                    map[packageName]?.plus(generatedGsonAdapter) ?: listOf(generatedGsonAdapter)

            return@fold map.plus(Pair(packageName, newList))
        }
    }
}