package gsonpath.adapter.standard.adapter.properties

import com.google.gson.FieldNamingPolicy
import gsonpath.AutoGsonAdapter
import gsonpath.GsonFieldValidationType
import gsonpath.LazyFactoryMetadata
import gsonpath.ProcessingException
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class AdapterCommonPropertiesFactory {

    fun create(
            modelElement: TypeElement,
            adapterAnnotation: AutoGsonAdapter,
            lazyFactoryMetadata: LazyFactoryMetadata): AdapterCommonProperties {

        val propertyFetcher = PropertyFetcher(modelElement)

        val flattenDelimiter = propertyFetcher.getProperty("flattenDelimiter",
                adapterAnnotation.flattenDelimiter.toTypedArray(),
                lazyFactoryMetadata.annotation.flattenDelimiter)

        val serializeNulls = propertyFetcher.getProperty("serializeNulls",
                adapterAnnotation.serializeNulls.toTypedArray(),
                lazyFactoryMetadata.annotation.serializeNulls)

        val fieldNamingPolicy = propertyFetcher.getProperty("fieldNamingPolicy",
                adapterAnnotation.fieldNamingPolicy,
                lazyFactoryMetadata.annotation.fieldNamingPolicy)

        val fieldValidationType = propertyFetcher.getProperty("fieldValidationType",
                adapterAnnotation.fieldValidationType,
                lazyFactoryMetadata.annotation.fieldValidationType)

        return AdapterCommonProperties(flattenDelimiter, serializeNulls, fieldNamingPolicy, fieldValidationType)
    }

    private class PropertyFetcher(val adapterElement: Element) {

        fun <T> getProperty(
                propertyName: String,
                adapterArray: Array<T>,
                factoryProperty: T
        ): T {
            if (adapterArray.size > 1) {
                throw ProcessingException("$propertyName should never have more than one element", adapterElement)
            }

            return adapterArray.firstOrNull() ?: factoryProperty
        }
    }
}

data class AdapterCommonProperties(
        val flattenDelimiter: Char,
        val serializeNulls: Boolean,
        val fieldNamingPolicy: FieldNamingPolicy,
        val fieldValidationType: GsonFieldValidationType)