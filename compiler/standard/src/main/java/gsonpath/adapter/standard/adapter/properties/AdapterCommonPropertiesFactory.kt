package gsonpath.adapter.standard.adapter.properties

import com.google.gson.FieldNamingPolicy
import gsonpath.GsonFieldValidationType
import gsonpath.LazyFactoryMetadata
import gsonpath.annotation.AutoGsonAdapter
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
}

data class AdapterCommonProperties(
        val flattenDelimiter: Char,
        val serializeNulls: Boolean,
        val fieldNamingPolicy: FieldNamingPolicy,
        val fieldValidationType: GsonFieldValidationType)